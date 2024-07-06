package io.github.bivashy.wttj.telegram.bot;

import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import io.github.bivashy.wttj.telegram.bot.listener.MessageUpdateListener;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.*;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class TelegramBotUpdatesListenerTest {

    @Spy
    @InjectMocks
    private TelegramBotUpdatesListener updatesListener;
    @Mock
    private MessageUpdateListener messageUpdateListener;

    @Test
    void testOnEmptyUpdate() {
        Update update = mock(Update.class);
        List<Update> updates = Collections.singletonList(update);

        updatesListener.process(updates);

        verify(updatesListener, times(1)).onUpdate(update);
        verify(messageUpdateListener, times(0)).onMessageUpdate(update);
    }

    @Test
    void testOnMessageUpdate() {
        Update update = mock(Update.class);
        Message message = mock(Message.class);
        when(update.message()).thenReturn(message);
        List<Update> updates = Collections.singletonList(update);

        updatesListener.process(updates);

        verify(updatesListener, times(1)).onUpdate(update);
        verify(messageUpdateListener, times(1)).onMessageUpdate(update);
    }

}
