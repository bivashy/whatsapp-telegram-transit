package io.github.bivashy.wttj.api.command.interactive;

import io.github.bivashy.wttj.api.command.Actor;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

public interface InteractiveCommandExtension<T extends Actor> {

    /**
     * Consumes user input and passes to the current interaction context, if any.
     *
     * @return Returns {@code true} if consume was successful, otherwise {@code false}.
     */
    boolean consumeInput(T actor, String input);

    CompletableFuture<String> awaitInput(T actor, long timeout, TimeUnit unit);

}
