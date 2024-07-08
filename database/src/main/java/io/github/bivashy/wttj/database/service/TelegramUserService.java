package io.github.bivashy.wttj.database.service;

import io.github.bivashy.wttj.database.model.TelegramUser;

public interface TelegramUserService {

    boolean exists(long userId);

    default TelegramUser find(long userId) {
        return find(userId, false);
    }

    TelegramUser find(long userId, boolean fetchSessions);

    void save(TelegramUser user);

}
