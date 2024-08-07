package io.github.bivashy.wttj.telegram.bot.command;

import io.github.bivashy.wttj.api.command.ExtendedCommand;
import io.github.bivashy.wttj.telegram.bot.command.actor.TelegramActor;
import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Model.CommandSpec;
import picocli.CommandLine.Parameters;
import picocli.CommandLine.Spec;

@Command(name = "/link")
public class LinkStartWhatsappCommand implements Runnable, ExtendedCommand<TelegramActor> {

    private final TelegramActor actor;
    @Parameters(arity = "0..1", prompt = "Valid values: QR, PAIRING", interactive = true)
    private LinkMode linkMode;
    @Spec
    private CommandSpec spec;

    public LinkStartWhatsappCommand(TelegramActor actor) {
        this.actor = actor;
    }

    @Override
    public void run() {
        actor.reply("You've chosen link mode " + linkMode);
    }

    @Override
    public TelegramActor actor() {
        return actor;
    }

    public enum LinkMode {
        QR("/linkqr"), PAIRING("/linkpair");
        final String command;

        LinkMode(String command) {
            this.command = command;
        }

        public void execute(CommandLine line) {
            line.execute(command);
        }
    }

}
