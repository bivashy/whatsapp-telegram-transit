package io.github.bivashy.telegram.bot.command.exception;

import io.github.bivashy.command.api.ExtendedCommand;
import io.github.bivashy.telegram.bot.command.actor.TelegramActor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import picocli.CommandLine;
import picocli.CommandLine.IExecutionExceptionHandler;
import picocli.CommandLine.ParseResult;

@Component
public class ExecutionExceptionHandler implements IExecutionExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(ExecutionExceptionHandler.class);

    @Override
    public int handleExecutionException(Exception e, CommandLine commandLine, ParseResult parseResult) throws Exception {
        ExtendedCommand<TelegramActor> extendedCommand = commandLine.getCommand();
        TelegramActor actor = extendedCommand.actor();
        actor.reply("Unexpected error occurred during command execution!");
        log.error("Error occurred during command execution", e);
        return 0;
    }

}
