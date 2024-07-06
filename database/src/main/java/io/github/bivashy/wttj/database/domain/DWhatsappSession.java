package io.github.bivashy.wttj.database.domain;

import io.ebean.annotation.NotNull;
import io.github.bivashy.wttj.database.model.WhatsappSession;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;

import java.util.UUID;

@Entity
public class DWhatsappSession extends BaseDomain implements WhatsappSession {

    @NotNull
    @Column(unique = true)
    private UUID sessionUniqueId;
    @ManyToOne(optional = false)
    private DTelegramUser user;

    public DWhatsappSession(UUID sessionUniqueId, DTelegramUser user) {
        this.sessionUniqueId = sessionUniqueId;
        this.user = user;
    }

    @Override
    public UUID getSessionUniqueId() {
        return sessionUniqueId;
    }

    @Override
    public DTelegramUser getUser() {
        return user;
    }

}
