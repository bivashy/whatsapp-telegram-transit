package io.github.bivashy.telegram.bot.command;

import io.github.bivashy.command.api.Actor;
import picocli.CommandLine;

public interface CommandLineFactory<T extends Actor> {

    CommandLine create(T actor);

}
