package io.github.bivashy.wttj.database.model;

import java.util.List;

public interface TelegramUser {

    long getUserId();

    void addSession(WhatsappSession session);

    List<WhatsappSession> getSessions();

}
