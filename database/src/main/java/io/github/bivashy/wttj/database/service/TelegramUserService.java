package io.github.bivashy.wttj.database.service;

import io.github.bivashy.wttj.database.model.TelegramUser;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface TelegramUserService {

    default List<? extends TelegramUser> all() {
        return all(false);
    }

    List<? extends TelegramUser> all(boolean fetchSessions);

    boolean exists(long userId);

    default Optional<TelegramUser> find(long userId) {
        return find(userId, false);
    }

    Optional<TelegramUser> find(long userId, boolean fetchSessions);

    void createOrAppend(long id, UUID sessionUniqueId);

    void save(TelegramUser user);

}
