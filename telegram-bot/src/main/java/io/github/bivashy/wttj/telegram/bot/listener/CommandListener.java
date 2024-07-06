package io.github.bivashy.wttj.telegram.bot.listener;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import io.github.bivashy.wttj.api.command.interactive.InteractiveCommandExtension;
import io.github.bivashy.wttj.telegram.bot.command.actor.TelegramActor;
import io.github.bivashy.wttj.telegram.bot.command.factory.CommandLineFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import picocli.CommandLine;

import java.util.concurrent.ExecutorService;

import static java.util.Objects.requireNonNull;

@Component
public class CommandListener {

    private static final Logger log = LoggerFactory.getLogger(CommandListener.class);
    private final TelegramBot bot;
    private final ExecutorService executorService;
    private final CommandLineFactory<TelegramActor> commandLineFactory;
    private final InteractiveCommandExtension<TelegramActor> interactiveCommandExtension;

    public CommandListener(TelegramBot bot,
                           @Qualifier("telegramCommandExecutor") ExecutorService executorService,
                           CommandLineFactory<TelegramActor> commandLineFactory,
                           InteractiveCommandExtension<TelegramActor> interactiveCommandExtension) {
        this.bot = bot;
        this.executorService = executorService;
        this.commandLineFactory = commandLineFactory;
        this.interactiveCommandExtension = interactiveCommandExtension;
    }

    public void onCommand(Update update) {
        requireNonNull(update.message());
        requireNonNull(update.message().text());
        log.debug("Command event update {}", update);
        Message message = update.message();
        TelegramActor actor = TelegramActor.messageActor(update, bot);
        if (interactiveCommandExtension.consumeInput(actor, message.text()))
            return;
        String[] args = message.text().split("\\s+");
        CommandLine commandLine = commandLineFactory.create(actor);
        executorService.execute(() -> commandLine.execute(args));
    }

}
