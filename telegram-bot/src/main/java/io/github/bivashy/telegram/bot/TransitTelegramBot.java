package io.github.bivashy.telegram.bot;

import com.pengrad.telegrambot.TelegramBot;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class TransitTelegramBot {

    private static final Logger log = LoggerFactory.getLogger(TransitTelegramBot.class);
    private final TelegramBot bot;
    private final TelegramBotUpdatesListener updatesListener;

    public TransitTelegramBot(TelegramBot bot, TelegramBotUpdatesListener updatesListener) {
        this.bot = bot;
        this.updatesListener = updatesListener;
    }

    @PostConstruct
    public void start() {
        log.info("Telegram bot started");
        this.bot.setUpdatesListener(updatesListener);
    }

}
