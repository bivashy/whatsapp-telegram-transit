package io.github.bivashy.wttj.telegram.bot.listener;

import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import static java.util.Objects.requireNonNull;

@Component
public class MessageUpdateListener {

    private static final Logger log = LoggerFactory.getLogger(MessageUpdateListener.class);
    private final CommandListener commandListener;

    public MessageUpdateListener(CommandListener commandListener) {
        this.commandListener = commandListener;
    }

    public void onMessageUpdate(Update update) {
        requireNonNull(update.message());
        log.debug("Message event update {}", update.message());
        Message message = update.message();
        String text = message.text();
        if (text != null)
            commandListener.onCommand(update);
    }

}
