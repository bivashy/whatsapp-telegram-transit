package io.github.bivashy.wttj.telegram.bot.command.telegram;

import io.github.bivashy.wttj.api.command.ExtendedCommand;
import io.github.bivashy.wttj.api.command.interactive.InteractiveCommandExtension;
import io.github.bivashy.wttj.telegram.bot.command.actor.TelegramActor;
import org.springframework.stereotype.Component;
import picocli.CommandLine.IDefaultValueProvider;
import picocli.CommandLine.MissingParameterException;
import picocli.CommandLine.Model.ArgSpec;
import picocli.CommandLine.Model.CommandSpec;

import java.util.concurrent.TimeUnit;

@Component
public class InteractiveTelegramDefaultProvider implements IDefaultValueProvider {

    // TODO: Make this configurable from properties
    private static final long TIMEOUT_DURATION = 30;
    private static final TimeUnit TIMEOUT_UNIT = TimeUnit.SECONDS;
    private final InteractiveCommandExtension<TelegramActor> interactiveCommandExtension;

    public InteractiveTelegramDefaultProvider(InteractiveCommandExtension<TelegramActor> interactiveCommandExtension) {
        this.interactiveCommandExtension = interactiveCommandExtension;
    }

    @Override
    public String defaultValue(ArgSpec argSpec) throws Exception {
        if (!argSpec.interactive())
            return null;
        CommandSpec commandSpec = argSpec.command();
        ExtendedCommand<TelegramActor> command = commandSpec.commandLine().getCommand();
        TelegramActor actor = command.actor();

        actor.reply("Enter value for " + argSpec.paramLabel());
        if (!argSpec.prompt().isEmpty())
            actor.reply(argSpec.prompt());
        String input = interactiveCommandExtension.awaitInput(actor, TIMEOUT_DURATION, TIMEOUT_UNIT).join();
        if (input == null)
            throw new MissingParameterException(commandSpec.commandLine(), argSpec, "Interactive argument input time out!");
        return input;
    }

}
