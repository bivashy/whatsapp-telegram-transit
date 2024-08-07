package io.github.bivashy.wttj.telegram.bot.configuration;

import io.ebean.Database;
import io.ebean.DatabaseFactory;
import io.ebean.config.DatabaseConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DatabaseConfiguration {

    @Bean
    public Database database(DatabaseConfig config) {
        return DatabaseFactory.create(config);
    }

    @Bean
    public DatabaseConfig databaseConfig() {
        DatabaseConfig config = new DatabaseConfig();
        config.loadFromProperties();
        return config;
    }

}
