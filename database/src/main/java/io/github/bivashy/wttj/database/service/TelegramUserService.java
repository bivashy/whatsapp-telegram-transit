package io.github.bivashy.wttj.database.service;

import io.github.bivashy.wttj.database.model.TelegramUser;

import java.util.List;

public interface TelegramUserService {

    default List<? extends TelegramUser> all() {
        return all(false);
    }

    List<? extends TelegramUser> all(boolean fetchSessions);

    boolean exists(long userId);

    default TelegramUser find(long userId) {
        return find(userId, false);
    }

    TelegramUser find(long userId, boolean fetchSessions);

    void save(TelegramUser user);

}
