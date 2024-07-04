package io.github.bivashy;

import com.pengrad.telegrambot.model.Update;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.*;
import org.mockito.InjectMocks;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class TelegramBotUpdatesListenerTest {

    @Spy
    @InjectMocks
    private TelegramBotUpdatesListener updatesListener;

    @Test
    void testDebugUpdate() {
        // Given
        Update update = mock(Update.class);
        List<Update> updates = Collections.singletonList(update);

        // When
        updatesListener.process(updates);

        // Then
        verify(updatesListener, times(1)).onUpdate(update);
    }

}
