package io.github.bivashy.wttj.telegram.bot.command;

import com.pengrad.telegrambot.request.SendPhoto;
import io.github.bivashy.wttj.api.command.ExtendedCommand;
import io.github.bivashy.wttj.database.domain.DTelegramUser;
import io.github.bivashy.wttj.database.domain.DWhatsappSession;
import io.github.bivashy.wttj.database.service.TelegramUserService;
import io.github.bivashy.wttj.telegram.bot.command.actor.TelegramActor;
import io.github.bivashy.wttj.telegram.bot.command.service.WhatsappConnectionService;
import it.auties.whatsapp.api.QrHandler;
import it.auties.whatsapp.api.Whatsapp;
import it.auties.whatsapp.model.message.model.MessageContainer;
import it.auties.whatsapp.model.message.standard.TextMessage;
import picocli.CommandLine.Command;

import java.util.UUID;

@Command(name = "/linkqr")
public record LinkQrWhatsappCommand(TelegramActor actor, TelegramUserService userService, WhatsappConnectionService connectionService) implements Runnable, ExtendedCommand<TelegramActor> {

    @Override
    public void run() {
        UUID sessionUniqueId = UUID.randomUUID();

        Whatsapp whatsapp = Whatsapp.webBuilder()
                .newConnection(sessionUniqueId)
                .unregistered(QrHandler.toFile(path -> actor.execute(new SendPhoto(actor.chatId(), path.toFile()))));

        whatsapp.addLoggedInListener(() -> actor.reply("Successfully logged in into account!"));
        whatsapp.addNewChatMessageListener(messageEvent -> {
            MessageContainer message = messageEvent.message();
            String textContent = message.textMessage().map(TextMessage::text).orElse(message.textWithNoContextMessage().orElse("<no text>"));
            actor.reply("New message with text '" + textContent + "'");
        });

        DTelegramUser user = new DTelegramUser(actor.getId());
        user.addSession(new DWhatsappSession(sessionUniqueId, user));
        userService.save(user);

        connectionService.registerAndConnect(sessionUniqueId, whatsapp);
    }

}
