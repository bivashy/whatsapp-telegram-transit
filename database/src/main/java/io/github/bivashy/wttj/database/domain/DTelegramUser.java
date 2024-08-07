package io.github.bivashy.wttj.database.domain;

import io.ebean.annotation.NotNull;
import io.github.bivashy.wttj.database.model.TelegramUser;
import io.github.bivashy.wttj.database.model.WhatsappSession;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Entity
public class DTelegramUser extends BaseDomain implements TelegramUser {

    @NotNull
    @Column(unique = true)
    private long userId;
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<DWhatsappSession> sessions;

    public DTelegramUser(long userId) {
        this.userId = userId;
    }

    @Override
    public void addSession(WhatsappSession session) {
        if (sessions == null)
            sessions = new ArrayList<>();
        sessions.add((DWhatsappSession) session);
    }

    @Override
    public long getUserId() {
        return userId;
    }

    @Override
    public List<WhatsappSession> getSessions() {
        return Collections.unmodifiableList(sessions);
    }

}
