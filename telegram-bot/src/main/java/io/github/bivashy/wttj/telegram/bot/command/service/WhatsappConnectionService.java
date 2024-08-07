package io.github.bivashy.wttj.telegram.bot.command.service;

import io.github.bivashy.wttj.database.service.WhatsappSessionService;
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

    public WhatsappConnectionService(WhatsappSessionService sessionService) {
        this.sessionService = sessionService;
    }

    @PostConstruct
    void initConnections() {
        sessionService.each(whatsappSession -> {
            UUID sessionUniqueId = whatsappSession.getSessionUniqueId();
            Optional<Whatsapp> whatsappOptional = Whatsapp.webBuilder()
                    .newConnection(sessionUniqueId)
                    .registered();
            if (whatsappOptional.isEmpty()) {
                log.warn("Session with UID {} not found", sessionUniqueId);
                return;
            }
            Whatsapp whatsapp = whatsappOptional.get();
            // TODO: Rate limiting
            connect(sessionUniqueId, whatsapp);
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
