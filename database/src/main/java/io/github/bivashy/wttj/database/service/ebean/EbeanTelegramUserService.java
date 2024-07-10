package io.github.bivashy.wttj.database.service.ebean;

import io.ebean.Database;
import io.ebean.Transaction;
import io.github.bivashy.wttj.database.domain.DTelegramUser;
import io.github.bivashy.wttj.database.domain.DWhatsappSession;
import io.github.bivashy.wttj.database.domain.query.QDTelegramUser;
import io.github.bivashy.wttj.database.model.TelegramUser;
import io.github.bivashy.wttj.database.service.TelegramUserService;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static java.util.Objects.requireNonNull;

public class EbeanTelegramUserService implements TelegramUserService {

    private final Database database;

    public EbeanTelegramUserService(Database database) {
        this.database = database;
    }

    @Override
    public List<? extends TelegramUser> all(boolean fetchSessions) {
        QDTelegramUser query = new QDTelegramUser();
        if (fetchSessions)
            query = query.sessions.fetchCache();
        return query.findList();
    }

    @Override
    public boolean exists(long userId) {
        return userId(userId).exists();
    }

    @Override
    public Optional<TelegramUser> find(long userId, boolean fetchSessions) {
        QDTelegramUser query = userId(userId);
        if (fetchSessions)
            query = query.sessions.fetchCache();
        return Optional.ofNullable(query.findOne());
    }

    @Override
    public void createOrAppend(long id, UUID sessionUniqueId) {
        try (Transaction transaction = database.beginTransaction()) {
            Optional<TelegramUser> existingOptional = find(id);
            if (existingOptional.isPresent()) {
                DTelegramUser existing = (DTelegramUser) existingOptional.get();
                DWhatsappSession session = new DWhatsappSession(sessionUniqueId, existing);
                database.save(session);
            } else {
                DTelegramUser newUser = new DTelegramUser(id);
                newUser.addSession(new DWhatsappSession(sessionUniqueId, newUser));
                database.save(newUser);
            }

            transaction.commit();
        }
    }

    @Override
    public void save(TelegramUser user) {
        requireNonNull(user);
        if (!(user instanceof DTelegramUser))
            throw new IllegalArgumentException("Cannot save non-domain user '" + user.getClass().getName() + "'");
        database.save(user);
    }

    private QDTelegramUser userId(long userId) {
        return new QDTelegramUser().usingDatabase(database).userId.eq(userId);
    }

}
