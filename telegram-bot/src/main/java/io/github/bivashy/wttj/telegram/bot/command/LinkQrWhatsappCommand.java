package io.github.bivashy.wttj.telegram.bot.command;

import com.pengrad.telegrambot.request.SendPhoto;
import io.github.bivashy.wttj.database.service.TelegramUserService;
import io.github.bivashy.wttj.telegram.bot.command.actor.TelegramActor;
import io.github.bivashy.wttj.telegram.bot.command.service.WhatsappConnectionService;
import it.auties.whatsapp.api.ErrorHandler;
import it.auties.whatsapp.api.QrHandler;
import it.auties.whatsapp.api.WebOptionsBuilder;
import it.auties.whatsapp.api.Whatsapp;
import picocli.CommandLine.Command;

@Command(name = "/linkqr")
public class LinkQrWhatsappCommand extends LinkWhatsappCommand {

    public LinkQrWhatsappCommand(TelegramActor actor, TelegramUserService userService,
                                 WhatsappConnectionService connectionService,
                                 ErrorHandler errorHandler) {
        super(actor, userService, connectionService, errorHandler);
    }

    @Override
    protected Whatsapp extend(WebOptionsBuilder builder, TelegramActor actor) {
        return builder.unregistered(QrHandler.toFile(path -> actor.execute(new SendPhoto(actor.chatId(), path.toFile()))));
    }

}
