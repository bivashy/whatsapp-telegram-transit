package io.github.bivashy.wttj.telegram.bot.command;

import com.pengrad.telegrambot.request.SendPhoto;
import io.github.bivashy.wttj.api.command.ExtendedCommand;
import io.github.bivashy.wttj.database.domain.DTelegramUser;
import io.github.bivashy.wttj.database.domain.DWhatsappSession;
import io.github.bivashy.wttj.database.service.TelegramUserService;
import io.github.bivashy.wttj.telegram.bot.command.actor.TelegramActor;
import it.auties.whatsapp.api.QrHandler;
import it.auties.whatsapp.api.Whatsapp;
import picocli.CommandLine.Command;

import java.util.UUID;

@Command(name = "/linkqr")
public record LinkQrWhatsappCommand(TelegramActor actor, TelegramUserService userService) implements Runnable, ExtendedCommand<TelegramActor> {

    @Override
    public void run() {
        UUID sessionUniqueId = UUID.randomUUID();

        Whatsapp whatsapp = Whatsapp.webBuilder()
                .newConnection(sessionUniqueId)
                .unregistered(QrHandler.toFile(path -> actor.execute(new SendPhoto(actor.chatId(), path.toFile()))));

        whatsapp.addLoggedInListener(() -> actor.reply("Successfully logged in into account!"));

        DTelegramUser user = new DTelegramUser(actor.getId());
        user.addSession(new DWhatsappSession(sessionUniqueId, user));
        userService.save(user);

        whatsapp.connect();
    }

}
