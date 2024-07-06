package io.github.bivashy.wttj.telegram.bot;

import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.Update;
import io.github.bivashy.wttj.telegram.bot.listener.MessageUpdateListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class TelegramBotUpdatesListener implements UpdatesListener {

    private static final Logger log = LoggerFactory.getLogger(TelegramBotUpdatesListener.class);
    private final MessageUpdateListener messageUpdateListener;

    public TelegramBotUpdatesListener(MessageUpdateListener messageUpdateListener) {
        this.messageUpdateListener = messageUpdateListener;
    }

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

    void onUpdate(Update update) {
        if (update.message() != null)
            messageUpdateListener.onMessageUpdate(update);
        log.debug("New update {}", update);
    }

}
