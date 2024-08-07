package io.github.bivashy.wttj.telegram.bot.command;

import io.github.bivashy.wttj.database.service.TelegramUserService;
import io.github.bivashy.wttj.telegram.bot.command.actor.TelegramActor;
import io.github.bivashy.wttj.telegram.bot.command.service.WhatsappConnectionService;
import it.auties.whatsapp.api.ErrorHandler;
import it.auties.whatsapp.api.WebOptionsBuilder;
import it.auties.whatsapp.api.Whatsapp;
import picocli.CommandLine.Command;
import picocli.CommandLine.Parameters;

@Command(name = "/linkpair")
public class LinkPairWhatsappCommand extends LinkWhatsappCommand {

    @Parameters(arity = "0..1", interactive = true)
    private long number;

    public LinkPairWhatsappCommand(TelegramActor actor, TelegramUserService userService,
                                   WhatsappConnectionService connectionService,
                                   ErrorHandler errorHandler) {
        super(actor, userService, connectionService, errorHandler);
    }

    @Override
    protected Whatsapp extend(WebOptionsBuilder builder, TelegramActor actor) {
        return builder.unregistered(number, (code) -> actor.reply("Your code is '" + code + "'"));
    }

}
