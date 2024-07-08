package io.github.bivashy.wttj.telegram.bot.command.telegram;

import io.github.bivashy.wttj.database.service.TelegramUserService;
import io.github.bivashy.wttj.telegram.bot.command.InjectWhatsappCommand;
import io.github.bivashy.wttj.telegram.bot.command.LinkQrWhatsappCommand;
import io.github.bivashy.wttj.telegram.bot.command.LinkStartWhatsappCommand;
import io.github.bivashy.wttj.telegram.bot.command.ListWhatsappConnectionCommand;
import io.github.bivashy.wttj.telegram.bot.command.MainCommand;
import io.github.bivashy.wttj.telegram.bot.command.actor.TelegramActor;
import io.github.bivashy.wttj.telegram.bot.command.exception.ExecutionExceptionHandler;
import io.github.bivashy.wttj.telegram.bot.command.exception.ParameterExceptionHandler;
import io.github.bivashy.wttj.telegram.bot.command.factory.CommandLineFactory;
import io.github.bivashy.wttj.telegram.bot.command.service.WhatsappConnectionService;
import org.springframework.stereotype.Component;
import picocli.CommandLine;

@Component
public class TelegramCommandLineFactory implements CommandLineFactory<TelegramActor> {

    private final InteractiveTelegramDefaultProvider interactiveTelegramDefaultProvider;
    private final TelegramUserService userService;
    private final WhatsappConnectionService connectionService;

    public TelegramCommandLineFactory(InteractiveTelegramDefaultProvider interactiveTelegramDefaultProvider,
                                      TelegramUserService userService,
                                      WhatsappConnectionService connectionService) {
        this.interactiveTelegramDefaultProvider = interactiveTelegramDefaultProvider;
        this.userService = userService;
        this.connectionService = connectionService;
    }

    @Override
    public CommandLine create(TelegramActor actor) {
        return new CommandLine(new MainCommand(actor))
                .addSubcommand(new InjectWhatsappCommand(actor))
                .addSubcommand(new LinkStartWhatsappCommand(actor))
                .addSubcommand(new LinkQrWhatsappCommand(actor, userService, connectionService))
                .addSubcommand(new ListWhatsappConnectionCommand(actor, userService, connectionService))
                .setExecutionExceptionHandler(new ExecutionExceptionHandler())
                .setParameterExceptionHandler(new ParameterExceptionHandler())
                .setDefaultValueProvider(interactiveTelegramDefaultProvider);
    }

}
