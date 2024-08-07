package io.github.bivashy.wttj.telegram.bot.command;

import com.pengrad.telegrambot.model.request.InlineKeyboardButton;
import com.pengrad.telegrambot.model.request.InlineKeyboardMarkup;
import com.pengrad.telegrambot.request.SendMessage;
import io.github.bivashy.wttj.api.command.ExtendedCommand;
import io.github.bivashy.wttj.database.model.TelegramUser;
import io.github.bivashy.wttj.database.model.WhatsappSession;
import io.github.bivashy.wttj.database.service.TelegramUserService;
import io.github.bivashy.wttj.telegram.bot.command.actor.TelegramActor;
import io.github.bivashy.wttj.telegram.bot.command.service.WhatsappConnectionService;
import it.auties.whatsapp.api.Whatsapp;
import it.auties.whatsapp.controller.Store;
import picocli.CommandLine.Command;

import java.util.List;
import java.util.Optional;

@Command(name = "/list")
public record ListWhatsappConnectionCommand(TelegramActor actor,
        TelegramUserService userService,
        WhatsappConnectionService connectionService) implements Runnable, ExtendedCommand<TelegramActor> {

    @Override
    public void run() {
        Optional<TelegramUser> userOptional = userService.find(actor.getId(), true);
        if (userOptional.isEmpty()) {
            actor.reply("You don't have any whatsapp connections!");
            return;
        }
        TelegramUser user = userOptional.get();
        if (user.getSessions().isEmpty()) {
            actor.reply("No session found!");
            return;
        }
        List<Whatsapp> connections = user.getSessions()
                .stream()
                .map(WhatsappSession::getSessionUniqueId)
                .map(connectionService::findConnection)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .toList();
        InlineKeyboardButton[] buttons = connections.stream()
                .map(Whatsapp::store)
                .map(Store::name)
                .map(name -> new InlineKeyboardButton(name).callbackData(name))
                .toArray(InlineKeyboardButton[]::new);
        InlineKeyboardMarkup keyboardMarkup = new InlineKeyboardMarkup(buttons);
        actor.execute(new SendMessage(actor.chatId(), "Your accounts").replyMarkup(keyboardMarkup));
    }

}
