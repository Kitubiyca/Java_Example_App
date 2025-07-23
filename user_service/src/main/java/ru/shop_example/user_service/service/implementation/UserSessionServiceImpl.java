package ru.shop_example.user_service.service.implementation;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.shop_example.user_service.repository.SessionTokenRepository;
import ru.shop_example.user_service.service.UserSessionService;
import ru.shop_example.user_service.controller.UserSessionController;

import java.util.UUID;

/**
 * Реализация сервиса для работы с активными сессиями пользователя.
 *
 * @see UserSessionController
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class UserSessionServiceImpl implements UserSessionService {

    private final SessionTokenRepository sessionTokenRepository;

    /**
     * {@inheritDoc}
     */
    public void terminateCurrentSessionBySessionId(UUID sessionId){
        log.info("Called terminateCurrentSessionBySessionId service method");
        sessionTokenRepository.deleteAllBySessionId(sessionId);
    }

    /**
     * {@inheritDoc}
     */
    public void terminateAllSessionsByUserId(UUID userId){
        log.info("Called terminateAllSessionsByUserId service method");
        sessionTokenRepository.deleteAllByUserId(userId);
    }

}
