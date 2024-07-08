package io.github.bivashy.wttj.telegram.bot.command.actor;

import com.pengrad.telegrambot.Callback;
import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Chat;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.BaseRequest;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.response.BaseResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public record TelegramMessageActor(Update update, TelegramBot bot) implements TelegramActor {

    private static final Logger log = LoggerFactory.getLogger(TelegramMessageActor.class);

    @Override
    public long getId() {
        return update.message().from().id();
    }

    @Override
    public void reply(String message) {
        SendMessage sendMessage = new SendMessage(chatId(), message);
        execute(sendMessage);
    }

    @Override
    public <T extends BaseRequest<T, R>, R extends BaseResponse> void execute(T request) {
        bot.execute(request, new Callback<T, R>() {
            @Override
            public void onResponse(T baseRequest, R response) {
                if (response.isOk())
                    return;
                log.warn("Executing request failed, chatId '{}', error-code '{}', description '{}'", chatId(), response.errorCode(),
                        response.description());
            }

            @Override
            public void onFailure(T sendMessage, IOException e) {
                log.warn("Executing request was failed due to IOException", e);
            }
        });
    }

    @Override
    public long chatId() {
        return chat().id();
    }

    @Override
    public Chat chat() {
        return update.message().chat();
    }

}
