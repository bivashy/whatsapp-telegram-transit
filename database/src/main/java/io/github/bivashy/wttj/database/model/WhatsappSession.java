package io.github.bivashy.wttj.database.model;

import java.util.UUID;

public interface WhatsappSession {

    UUID getSessionUniqueId();

    TelegramUser getUser();

}
