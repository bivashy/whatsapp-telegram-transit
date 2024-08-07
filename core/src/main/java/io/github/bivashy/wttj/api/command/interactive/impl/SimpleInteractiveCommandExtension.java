package io.github.bivashy.wttj.api.command.interactive.impl;

import io.github.bivashy.wttj.api.command.Actor;
import io.github.bivashy.wttj.api.command.interactive.InteractiveCommandExtension;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class SimpleInteractiveCommandExtension<T extends Actor> implements InteractiveCommandExtension<T> {

    private static final Logger log = LoggerFactory.getLogger(SimpleInteractiveCommandExtension.class);
    private final Map<Long, CompletableFuture<String>> inputAwaits = new HashMap<>();

    @Override
    public boolean consumeInput(T actor, String input) {
        if (!inputAwaits.containsKey(actor.getId()))
            return false;
        CompletableFuture<String> future = inputAwaits.get(actor.getId());
        future.complete(input);
        inputAwaits.remove(actor.getId());
        return true;
    }

    @Override
    public CompletableFuture<String> awaitInput(T actor, long timeout, TimeUnit unit) {
        CompletableFuture<String> result = new CompletableFuture<String>()
                .orTimeout(timeout, unit)
                .exceptionally((throwable) -> {
                    if (throwable instanceof TimeoutException) {
                        inputAwaits.remove(actor.getId());
                        return null;
                    }
                    log.error("Error occurred during awaiting user input", throwable);
                    return null;
                });
        inputAwaits.put(actor.getId(), result);
        return result;
    }

}
