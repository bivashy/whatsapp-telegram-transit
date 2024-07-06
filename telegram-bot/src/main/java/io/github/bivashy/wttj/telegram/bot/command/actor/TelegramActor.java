package io.github.bivashy.wttj.telegram.bot.command.actor;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Chat;
import com.pengrad.telegrambot.model.Update;
import io.github.bivashy.wttj.api.command.Actor;

import static java.util.Objects.requireNonNull;

public interface TelegramActor extends Actor {

    static TelegramActor messageActor(Update update, TelegramBot bot) {
        requireNonNull(update.message());
        requireNonNull(bot);
        return new TelegramMessageActor(update, bot);
    }

    Update update();

    long chatId();

    Chat chat();

    TelegramBot bot();

}