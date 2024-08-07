package io.github.bivashy.wttj.telegram.bot.listener;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Chat;
import com.pengrad.telegrambot.model.Chat.Type;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import io.github.bivashy.wttj.api.command.interactive.InteractiveCommandExtension;
import io.github.bivashy.wttj.telegram.bot.command.factory.CommandLineFactory;
import io.github.bivashy.wttj.telegram.bot.command.InjectWhatsappCommand;
import io.github.bivashy.wttj.telegram.bot.command.MainCommand;
import io.github.bivashy.wttj.telegram.bot.command.actor.TelegramActor;
import io.github.bivashy.wttj.telegram.bot.command.actor.TelegramMessageActor;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.*;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import picocli.CommandLine;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Arrays;
import java.util.concurrent.ExecutorService;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CommandListenerTest {

    @InjectMocks
    private CommandListener commandListener;
    @Mock
    private CommandLineFactory<TelegramActor> commandLineFactory;
    @Mock
    private TelegramBot bot;
    @Mock
    private InteractiveCommandExtension<TelegramActor> interactiveCommandExtension;
    @Mock
    private ExecutorService executorService;

    @Test
    void testOnInvalidUpdate() {
        Update update = mock(Update.class);
        Update updateWithMessage = mock(Update.class);
        Message message = mock(Message.class);
        when(updateWithMessage.message()).thenReturn(message);

        assertThrows(NullPointerException.class, () ->
                commandListener.onCommand(update)
        );
        assertThrows(NullPointerException.class, () ->
                commandListener.onCommand(updateWithMessage)
        );
    }

    @Test
    void testWrongCommand() {
        String commandText = "test args";
        String[] commandArgs = new String[]{"test", "args"};

        Update update = createMessageUpdate(commandText);
        TelegramActor actor = Mockito.mock(TelegramMessageActor.class);
        CommandLine commandLine = createMockCommandLine(actor);

        when(commandLineFactory.create(any(TelegramActor.class))).thenReturn(commandLine);
        runCommand(update);

        verify(commandLine).execute(commandArgs);
        assertEquals(Arrays.asList(commandArgs), commandLine.getParseResult().unmatched());
    }

    @Test
    void testInjectCommandNonSupergroup() {
        String commandText = "/inject";
        String[] commandArgs = new String[]{"/inject"};

        Update update = createMessageUpdate(commandText);
        TelegramActor actor = mock(TelegramMessageActor.class);
        CommandLine commandLine = createMockCommandLine(actor);

        when(commandLineFactory.create(any(TelegramActor.class))).thenReturn(commandLine);
        runCommand(update);

        verify(commandLine).execute(commandArgs);
        verify(actor).reply("Inject command could be executed only in supergroup!");
    }

    @Test
    void testInjectCommandNotForum() {
        String commandText = "/inject";
        String[] commandArgs = new String[]{"/inject"};

        Update update = createMessageUpdate(commandText);
        TelegramActor actor = mock(TelegramMessageActor.class);
        Chat chat = mock(Chat.class);
        when(actor.chat()).thenReturn(chat);
        when(chat.type()).thenReturn(Type.supergroup);
        CommandLine commandLine = createMockCommandLine(actor);

        when(commandLineFactory.create(any(TelegramActor.class))).thenReturn(commandLine);
        runCommand(update);

        verify(commandLine).execute(commandArgs);
        verify(actor).reply("Please enable 'topics' in supergroup settings.");
    }

    private void runCommand(Update update) {
        ArgumentCaptor<Runnable> executeCaptor = ArgumentCaptor.forClass(Runnable.class);

        commandListener.onCommand(update);

        verify(executorService).execute(executeCaptor.capture());
        executeCaptor.getValue().run();
    }

    private Update createMessageUpdate(String command) {
        Update update = mock(Update.class);
        Message message = mock(Message.class);
        when(update.message()).thenReturn(message);
        when(message.text()).thenReturn(command);
        return update;
    }

    private CommandLine createMockCommandLine(TelegramActor actor) {
        PrintWriter printWriter = new PrintWriter(new StringWriter());
        return spy(new CommandLine(new MainCommand(actor))
                .addSubcommand(new InjectWhatsappCommand(actor))
                .setOut(printWriter)
                .setErr(printWriter));
    }

}
