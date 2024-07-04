package io.github.bivashy.telegram.bot.configuration;

import com.pengrad.telegrambot.TelegramBot;
import io.github.bivashy.telegram.bot.properties.TelegramBotProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TelegramBotConfiguration {

    @Bean
    public TelegramBot telegramBot(TelegramBotProperties properties) {
        if (!properties.hasToken())
            throw new IllegalArgumentException("Telegram bot token not present");
        return new TelegramBot(properties.getToken());
    }

}
