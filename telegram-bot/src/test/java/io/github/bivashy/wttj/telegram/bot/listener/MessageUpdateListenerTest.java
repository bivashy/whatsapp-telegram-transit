package io.github.bivashy.wttj.telegram.bot.listener;

import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.*;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class MessageUpdateListenerTest {

    @InjectMocks
    private MessageUpdateListener updatesListener;
    @Mock
    private CommandListener commandListener;

    @Test
    void testOnMessageWithoutTextUpdate() {
        Update update = mock(Update.class);
        Message message = mock(Message.class);
        when(update.message()).thenReturn(message);

        updatesListener.onMessageUpdate(update);

        verify(commandListener, times(0)).onCommand(update);
    }

    @Test
    void testOnMessageWithTextUpdate() {
        Update update = mock(Update.class);
        Message message = mock(Message.class);
        when(update.message()).thenReturn(message);
        when(message.text()).thenReturn("foo bar");

        updatesListener.onMessageUpdate(update);

        verify(commandListener, times(1)).onCommand(update);
    }

    @Test
    void testOnInvalidUpdate() {
        Update update = mock(Update.class);

        assertThrows(NullPointerException.class, () ->
                updatesListener.onMessageUpdate(update)
        );
    }

}
