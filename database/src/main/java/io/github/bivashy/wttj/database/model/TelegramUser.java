package io.github.bivashy.wttj.database.model;

import java.util.List;

public interface TelegramUser {

    long getUserId();

    List<WhatsappSession> getSessions();

}
