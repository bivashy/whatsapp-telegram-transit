package io.github.bivashy.wttj.telegram.bot.command.telegram;

import io.github.bivashy.wttj.database.service.TelegramUserService;
import io.github.bivashy.wttj.telegram.bot.command.InjectWhatsappCommand;
import io.github.bivashy.wttj.telegram.bot.command.LinkQrWhatsappCommand;
import io.github.bivashy.wttj.telegram.bot.command.LinkStartWhatsappCommand;
import io.github.bivashy.wttj.telegram.bot.command.MainCommand;
import io.github.bivashy.wttj.telegram.bot.command.actor.TelegramActor;
import io.github.bivashy.wttj.telegram.bot.command.exception.ExecutionExceptionHandler;
import io.github.bivashy.wttj.telegram.bot.command.exception.ParameterExceptionHandler;
import io.github.bivashy.wttj.telegram.bot.command.factory.CommandLineFactory;
import org.springframework.stereotype.Component;
import picocli.CommandLine;

@Component
public class TelegramCommandLineFactory implements CommandLineFactory<TelegramActor> {

    private final InteractiveTelegramDefaultProvider interactiveTelegramDefaultProvider;
    private final TelegramUserService userService;

    public TelegramCommandLineFactory(InteractiveTelegramDefaultProvider interactiveTelegramDefaultProvider, TelegramUserService userService) {
        this.interactiveTelegramDefaultProvider = interactiveTelegramDefaultProvider;
        this.userService = userService;
    }

    @Override
    public CommandLine create(TelegramActor actor) {
        return new CommandLine(new MainCommand(actor))
                .addSubcommand(new InjectWhatsappCommand(actor))
                .addSubcommand(new LinkStartWhatsappCommand(actor))
                .addSubcommand(new LinkQrWhatsappCommand(actor, userService))
                .setExecutionExceptionHandler(new ExecutionExceptionHandler())
                .setParameterExceptionHandler(new ParameterExceptionHandler())
                .setDefaultValueProvider(interactiveTelegramDefaultProvider);
    }

}
