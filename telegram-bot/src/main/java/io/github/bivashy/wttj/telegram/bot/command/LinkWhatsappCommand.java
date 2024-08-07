package io.github.bivashy.wttj.telegram.bot.command;

import io.github.bivashy.wttj.api.command.ExtendedCommand;
import io.github.bivashy.wttj.database.service.TelegramUserService;
import io.github.bivashy.wttj.telegram.bot.command.actor.TelegramActor;
import io.github.bivashy.wttj.telegram.bot.command.service.WhatsappConnectionService;
import it.auties.whatsapp.api.ErrorHandler;
import it.auties.whatsapp.api.WebOptionsBuilder;
import it.auties.whatsapp.api.Whatsapp;
import it.auties.whatsapp.model.message.model.MessageContainer;
import it.auties.whatsapp.model.message.standard.TextMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public abstract class LinkWhatsappCommand implements Runnable, ExtendedCommand<TelegramActor> {

    private static final long LINK_TIMEOUT_DELAY = 30;
    private static final TimeUnit LINK_TIMEOUT_UNIT = TimeUnit.SECONDS;
    private static final Logger log = LoggerFactory.getLogger(LinkWhatsappCommand.class);
    private final TelegramActor actor;
    private final TelegramUserService userService;
    private final WhatsappConnectionService connectionService;
    private final ErrorHandler errorHandler;

    public LinkWhatsappCommand(TelegramActor actor, TelegramUserService userService, WhatsappConnectionService connectionService, ErrorHandler errorHandler) {
        this.actor = actor;
        this.userService = userService;
        this.connectionService = connectionService;
        this.errorHandler = errorHandler;
    }

    @Override
    public void run() {
        UUID sessionUniqueId = UUID.randomUUID();

        Whatsapp whatsapp = extend(Whatsapp.webBuilder()
                .newConnection(sessionUniqueId)
                .errorHandler(errorHandler), actor);

        CompletableFuture<Void> loginFuture = new CompletableFuture<>();
        whatsapp.addLoggedInListener(() -> {
            actor.reply("Successfully logged in into account!");
            userService.createOrAppend(actor.getId(), sessionUniqueId);
            loginFuture.complete(null);
        });
        whatsapp.addNewChatMessageListener(messageEvent -> {
            MessageContainer message = messageEvent.message();
            String textContent = message.textMessage().map(TextMessage::text).orElse(message.textWithNoContextMessage().orElse("<no text>"));
            actor.reply("New message with text '" + textContent + "'");
        });

        CompletableFuture<Whatsapp> connectionFuture = connectionService.registerAndConnect(sessionUniqueId, whatsapp).exceptionally(throwable -> {
            if (throwable != null)
                log.error("Error occurred during connection", throwable);
            return null;
        });
        loginFuture.orTimeout(LINK_TIMEOUT_DELAY, LINK_TIMEOUT_UNIT).exceptionally(throwable -> {
            if (throwable instanceof TimeoutException) {
                connectionFuture.cancel(true);
                whatsapp.disconnect();
                actor.reply("Timed out!");
            }
            return null;
        });
    }

    @Override
    public TelegramActor actor() {
        return actor;
    }

    protected abstract Whatsapp extend(WebOptionsBuilder builder, TelegramActor actor);

}
