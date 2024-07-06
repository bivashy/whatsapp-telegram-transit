package io.github.bivashy.wttj.telegram.bot.command.actor;

import com.pengrad.telegrambot.Callback;
import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Chat;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.response.SendResponse;
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
        bot.execute(sendMessage, new Callback<SendMessage, SendResponse>() {
            @Override
            public void onResponse(SendMessage sendMessage, SendResponse response) {
                if (response.isOk())
                    return;
                log.warn("Sending message to '{}', with text '{}' failed, error-code '{}', description '{}'", chatId(), message, response.errorCode(),
                        response.description());
            }

            @Override
            public void onFailure(SendMessage sendMessage, IOException e) {
                log.warn("Sending message was failed due to IOException", e);
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
