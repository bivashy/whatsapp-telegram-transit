package io.github.bivashy.wttj.telegram.bot.command;

import com.pengrad.telegrambot.model.Chat;
import com.pengrad.telegrambot.model.Chat.Type;
import io.github.bivashy.wttj.api.command.ExtendedCommand;
import io.github.bivashy.wttj.telegram.bot.command.actor.TelegramActor;
import picocli.CommandLine.Command;

@Command(name = "/inject")
public record InjectWhatsappCommand(TelegramActor actor) implements Runnable, ExtendedCommand<TelegramActor> {

    @Override
    public void run() {
        Chat chat = actor.chat();
        if (chat == null || chat.type() != Type.supergroup) {
            actor.reply("Inject command could be executed only in supergroup!");
            return;
        }
        if (!chat.isForum()) {
            actor.reply("Please enable 'topics' in supergroup settings.");
            return;
        }
        actor.reply("Fake injection completed");
        // TODO Actually inject into forum
    }

}
