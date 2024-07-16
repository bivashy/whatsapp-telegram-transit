package io.github.bivashy.wttj.database.service.ebean;

import io.ebean.Database;
import io.github.bivashy.wttj.database.domain.DWhatsappSession;
import io.github.bivashy.wttj.database.domain.query.QDWhatsappSession;
import io.github.bivashy.wttj.database.model.WhatsappSession;
import io.github.bivashy.wttj.database.service.WhatsappSessionService;

import java.util.List;
import java.util.function.Consumer;

import static java.util.Objects.requireNonNull;

public class EbeanWhatsappSessionService implements WhatsappSessionService {

    private final Database database;

    public EbeanWhatsappSessionService(Database database) {
        this.database = database;
    }

    @Override
    public void each(Consumer<WhatsappSession> consumer) {
        new QDWhatsappSession().findEach(consumer::accept);
    }

    @Override
    public List<? extends WhatsappSession> all() {
        return new QDWhatsappSession().findList();
    }

    @Override
    public void save(WhatsappSession session) {
        requireNonNull(session);
        if (!(session instanceof DWhatsappSession))
            throw new IllegalArgumentException("Cannot save non-domain session '" + session.getClass().getName() + "'");
        database.save(session);
    }

}
