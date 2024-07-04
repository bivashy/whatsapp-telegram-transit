package io.github.bivashy;

import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.Update;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class TelegramBotUpdatesListener implements UpdatesListener {

    private static final Logger log = LoggerFactory.getLogger(TelegramBotUpdatesListener.class);

    @Override
    public int process(List<Update> updates) {
        for (Update update : updates)
            try {
                onUpdate(update);
            } catch (Exception e) {
                log.error("Error while processing update", e);
            }
        return UpdatesListener.CONFIRMED_UPDATES_ALL;
    }

    private void onUpdate(Update update) {
        log.debug("New update {}", update);
    }

}
