package io.github.bivashy.telegram.bot.listener;

import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.*;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CommandListenerTest {

    @InjectMocks
    private CommandListener commandListener;

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

}
