package io.github.bivashy.wttj.database.service;

import io.github.bivashy.wttj.database.model.WhatsappSession;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Consumer;

public interface WhatsappSessionService {

    void each(Consumer<WhatsappSession> consumer);

    List<? extends WhatsappSession> all();

    void save(WhatsappSession session);

    Optional<WhatsappSession> findBySessionUniqueId(UUID sessionUniqueId, boolean fetchUser);

}
