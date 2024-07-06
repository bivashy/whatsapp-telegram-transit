package io.github.bivashy.wttj.telegram.bot.command.exception;

import io.github.bivashy.wttj.api.command.ExtendedCommand;
import io.github.bivashy.wttj.telegram.bot.command.actor.TelegramActor;
import picocli.CommandLine.IParameterExceptionHandler;
import picocli.CommandLine.ParameterException;

public class ParameterExceptionHandler implements IParameterExceptionHandler {

    @Override
    public int handleParseException(ParameterException e, String[] strings) {
        ExtendedCommand<TelegramActor> extendedCommand = e.getCommandLine().getCommand();
        TelegramActor actor = extendedCommand.actor();
        actor.reply(e.getMessage());
        return 0;
    }

}
