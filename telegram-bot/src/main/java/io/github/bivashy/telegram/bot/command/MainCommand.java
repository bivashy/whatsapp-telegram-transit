package io.github.bivashy.telegram.bot.command;

import io.github.bivashy.command.api.ExtendedCommand;
import io.github.bivashy.telegram.bot.command.actor.TelegramActor;
import picocli.CommandLine.Command;

/*
 * Dummy class without any implementation. Useful for creating subcommands without "root" command.
 */
@Command
public record MainCommand(TelegramActor actor) implements Runnable, ExtendedCommand<TelegramActor> {

    @Override
    public void run() {
    }

}
