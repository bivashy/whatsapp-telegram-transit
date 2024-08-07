package io.github.bivashy.wttj.telegram.bot.configuration;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.request.SendMessage;
import io.github.bivashy.wttj.database.model.WhatsappSession;
import io.github.bivashy.wttj.database.service.WhatsappSessionService;
import it.auties.whatsapp.api.ErrorHandler;
import it.auties.whatsapp.api.ErrorHandler.Location;
import it.auties.whatsapp.api.ErrorHandler.Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class WhatsappConfiguration {

    private static final Logger log = LoggerFactory.getLogger(WhatsappConfiguration.class);

    @Bean
    public ErrorHandler errorHandler(TelegramBot bot, WhatsappSessionService sessionService) {
        return (whatsapp, location, throwable) -> {
            switch (location) {
                case UNKNOWN -> log.error("Whatsapp unknown error of user {}", whatsapp.store().name(), throwable);
                case LOGIN -> log.error("Whatsapp error during logging in", throwable);
                case CRYPTOGRAPHY -> log.error("Cryptographic error", throwable);
                case MEDIA_CONNECTION -> log.error("Cannot renew media connection", throwable);
                case STREAM -> log.error("Error in the stream", throwable);
                case PULL_APP_STATE -> log.error("Pull app state error", throwable);
                case PUSH_APP_STATE -> log.error("Push app state error", throwable);
                case INITIAL_APP_STATE_SYNC -> log.error("Initial app state sync error", throwable);
                case MESSAGE -> log.error("Message serialization/deserialization error", throwable);
                case HISTORY_SYNC -> log.error("History sync error", throwable);
                case RECONNECT -> log.error("Reconnect failed", throwable);
            }
            if (location != Location.MESSAGE)
                sessionService.findBySessionUniqueId(whatsapp.store().uuid(), true)
                        .map(WhatsappSession::getUser)
                        .ifPresent(telegramUser -> new SendMessage(telegramUser.getUserId(),
                                "Unexpected error occurred (" + location.name() + "). Try to relink your whatsapp account."));
            return Result.DISCARD;
        };
    }

}
