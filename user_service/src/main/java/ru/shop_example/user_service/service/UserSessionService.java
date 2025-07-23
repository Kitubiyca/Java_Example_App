package ru.shop_example.user_service.service;

import ru.shop_example.user_service.controller.UserSessionController;

import java.util.UUID;

/**
 * Сервис для работы с активными сессиями пользователя.
 *
 * @see UserSessionController
 */
public interface UserSessionService {

    /**
     * Закрытие текущей сессии пользователя.
     *
     * @param sessionId id сессии
     */
    void terminateCurrentSessionBySessionId(UUID sessionId);

    /**
     * Закрытие всех активных сессий пользователя.
     *
     * @param userId id пользователя
     */
    void terminateAllSessionsByUserId(UUID userId);
}
