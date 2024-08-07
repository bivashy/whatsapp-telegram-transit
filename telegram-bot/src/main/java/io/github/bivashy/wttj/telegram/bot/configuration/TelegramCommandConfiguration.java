package io.github.bivashy.wttj.telegram.bot.configuration;

import io.github.bivashy.wttj.api.command.interactive.InteractiveCommandExtension;
import io.github.bivashy.wttj.api.command.interactive.impl.SimpleInteractiveCommandExtension;
import io.github.bivashy.wttj.telegram.bot.command.actor.TelegramActor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Configuration
public class TelegramCommandConfiguration {

    @Bean
    public InteractiveCommandExtension<TelegramActor> interactiveCommandExtension() {
        return new SimpleInteractiveCommandExtension<>();
    }

    @Bean("telegramCommandExecutor")
    public ExecutorService commandExecutorService() {
        return Executors.newCachedThreadPool();
    }

}
