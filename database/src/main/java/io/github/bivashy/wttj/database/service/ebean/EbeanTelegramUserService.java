package io.github.bivashy.wttj.database.service.ebean;

import io.ebean.Database;
import io.github.bivashy.wttj.database.domain.DTelegramUser;
import io.github.bivashy.wttj.database.domain.query.QDTelegramUser;
import io.github.bivashy.wttj.database.model.TelegramUser;
import io.github.bivashy.wttj.database.service.TelegramUserService;

import java.util.List;
import java.util.Optional;

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
