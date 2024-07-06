package io.github.bivashy.wttj.telegram.bot.command;

import io.github.bivashy.wttj.api.command.Actor;
import picocli.CommandLine;

public interface CommandLineFactory<T extends Actor> {

    CommandLine create(T actor);

}
