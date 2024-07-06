package io.github.bivashy.wttj.telegram.bot.command.telegram;

import io.github.bivashy.wttj.telegram.bot.command.InjectWhatsappCommand;
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

    public TelegramCommandLineFactory(InteractiveTelegramDefaultProvider interactiveTelegramDefaultProvider) {
        this.interactiveTelegramDefaultProvider = interactiveTelegramDefaultProvider;
    }

    @Override
    public CommandLine create(TelegramActor actor) {
        return new CommandLine(new MainCommand(actor))
                .addSubcommand(new InjectWhatsappCommand(actor))
                .setExecutionExceptionHandler(new ExecutionExceptionHandler())
                .setParameterExceptionHandler(new ParameterExceptionHandler())
                .setDefaultValueProvider(interactiveTelegramDefaultProvider);
    }

}
