package io.github.bivashy;

import com.pengrad.telegrambot.TelegramBot;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.*;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class TransitTelegramBotTest {

    @Mock
    private TelegramBot telegramBot;
    @Mock
    private TelegramBotUpdatesListener updatesListener;
    @InjectMocks
    private TransitTelegramBot transitTelegramBot;

    @Test
    void testStartup() {
        // Given
        doNothing().when(telegramBot).setUpdatesListener(any(TelegramBotUpdatesListener.class));

        // When
        ReflectionTestUtils.invokeMethod(transitTelegramBot, "start");

        // Then
        verify(telegramBot).setUpdatesListener(updatesListener);
    }

}
