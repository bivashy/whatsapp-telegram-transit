package io.github.bivashy.wttj.database.service;

import io.github.bivashy.wttj.database.model.WhatsappSession;

import java.util.List;

public interface WhatsappSessionService {

    List<? extends WhatsappSession> all();

    void save(WhatsappSession session);

}
