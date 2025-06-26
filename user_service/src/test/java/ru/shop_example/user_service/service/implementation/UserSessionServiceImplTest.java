package ru.shop_example.user_service.service.implementation;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.shop_example.user_service.repository.SessionTokenRepository;

import java.util.UUID;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class UserSessionServiceImplTest {

    @Mock
    private SessionTokenRepository sessionTokenRepository;
    @Spy
    @InjectMocks
    private UserSessionServiceImpl userSessionService;

    @Test
    @DisplayName("Должен успешно закрывать сессию")
    void terminateCurrentSessionBySessionIdShouldInvokeRepositoryMethod(){
        //Arrange
        UUID sessionId = UUID.randomUUID();

        //Act
        userSessionService.terminateCurrentSessionBySessionId(sessionId);

        //Verify
        verify(sessionTokenRepository).deleteAllBySessionId(eq(sessionId));
    }

    @Test
    @DisplayName("Должен успешно закрывать все сессии пользователя")
    void terminateAllSessionsByUserIdShouldInvokeRepositoryMethod(){
        //Arrange
        UUID userId = UUID.randomUUID();

        //Act
        userSessionService.terminateAllSessionsByUserId(userId);

        //Verify
        verify(sessionTokenRepository).deleteAllByUserId(eq(userId));
    }
}
