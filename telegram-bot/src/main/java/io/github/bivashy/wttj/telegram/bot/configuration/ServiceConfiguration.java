package io.github.bivashy.wttj.telegram.bot.configuration;

import io.ebean.Database;
import io.github.bivashy.wttj.database.service.TelegramUserService;
import io.github.bivashy.wttj.database.service.WhatsappSessionService;
import io.github.bivashy.wttj.database.service.ebean.EbeanTelegramUserService;
import io.github.bivashy.wttj.database.service.ebean.EbeanWhatsappSessionService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ServiceConfiguration {

    @Bean
    public TelegramUserService telegramUserService(Database database) {
        return new EbeanTelegramUserService(database);
    }

    @Bean
    public WhatsappSessionService whatsappSessionService(Database database) {
        return new EbeanWhatsappSessionService(database);
    }

}
