package io.github.bivashy.wttj.telegram.bot.command.service;

import io.github.bivashy.wttj.database.service.WhatsappSessionService;
import io.github.resilience4j.ratelimiter.RateLimiter;
import io.github.resilience4j.ratelimiter.RateLimiterRegistry;
import io.github.resilience4j.retry.Retry;
import io.github.resilience4j.retry.RetryRegistry;
import it.auties.whatsapp.api.ErrorHandler;
import it.auties.whatsapp.api.Whatsapp;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@Service
public class WhatsappConnectionService {

    private static final Logger log = LoggerFactory.getLogger(WhatsappConnectionService.class);
    private final Map<UUID, Whatsapp> whatsappConnections = new HashMap<>();
    private final WhatsappSessionService sessionService;
    private final RateLimiter rateLimiter;
    private final Retry retry;
    private final ErrorHandler errorHandler;

    public WhatsappConnectionService(WhatsappSessionService sessionService, RateLimiterRegistry rateLimiterRegistry, RetryRegistry retryRegistry,
                                     ErrorHandler errorHandler) {
        this.sessionService = sessionService;
        this.rateLimiter = rateLimiterRegistry.rateLimiter("whatsapp-connection");
        this.retry = retryRegistry.retry("whatsapp-connection");
        this.errorHandler = errorHandler;
    }

    @PostConstruct
    void initConnections() {
        sessionService.each(whatsappSession -> {
            UUID sessionUniqueId = whatsappSession.getSessionUniqueId();
            Optional<Whatsapp> whatsappOptional = Whatsapp.webBuilder()
                    .newConnection(sessionUniqueId)
                    .errorHandler(errorHandler)
                    .registered();
            if (whatsappOptional.isEmpty()) {
                log.warn("Session with UID {} not found", sessionUniqueId);
                return;
            }
            Whatsapp whatsapp = whatsappOptional.get();
            RateLimiter.decorateRunnable(rateLimiter, Retry.decorateRunnable(retry, () -> connect(sessionUniqueId, whatsapp))).run();
        });
    }

    CompletableFuture<Whatsapp> connect(UUID sessionUniqueId, Whatsapp whatsapp) {
        return whatsapp.connect().whenComplete(((connection, throwable) -> {
            if (throwable != null) {
                log.error("Couldn't connect to session {}", sessionUniqueId, throwable);
                return;
            }
            whatsappConnections.put(sessionUniqueId, whatsapp);
        }));
    }

    public CompletableFuture<Whatsapp> registerAndConnect(UUID sessionUniqueId, Whatsapp whatsapp) {
        return connect(sessionUniqueId, whatsapp);
    }

    public Optional<Whatsapp> findConnection(UUID sessionUniqueId) {
        return Optional.ofNullable(whatsappConnections.get(sessionUniqueId));
    }

}
