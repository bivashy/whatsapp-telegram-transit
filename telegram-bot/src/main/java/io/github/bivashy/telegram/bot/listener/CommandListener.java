package io.github.bivashy.telegram.bot.listener;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import io.github.bivashy.telegram.bot.command.CommandLineFactory;
import io.github.bivashy.telegram.bot.command.actor.TelegramActor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import picocli.CommandLine;

import static java.util.Objects.requireNonNull;

@Component
public class CommandListener {

    private static final Logger log = LoggerFactory.getLogger(CommandListener.class);
    private final TelegramBot bot;
    private final CommandLineFactory<TelegramActor> commandLineFactory;

    public CommandListener(TelegramBot bot, CommandLineFactory<TelegramActor> commandLineFactory) {
        this.bot = bot;
        this.commandLineFactory = commandLineFactory;
    }

    public void onCommand(Update update) {
        requireNonNull(update.message());
        requireNonNull(update.message().text());
        log.debug("Command event update {}", update);
        Message message = update.message();
        TelegramActor actor = TelegramActor.messageActor(update, bot);
        String[] args = message.text().split("\\s+");
        CommandLine commandLine = commandLineFactory.create(actor);
        commandLine.execute(args);
    }

}
