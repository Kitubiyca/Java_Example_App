package ru.shop_example.user_service.repository;

import java.time.Duration;
import java.util.UUID;

/**
 * Репозиторий для работы с токенами авторизации.
 */
public interface SessionTokenRepository {

    /**
     * Получение refresh токена по id сессии.
     *
     * @param sessionId id сессии
     *
     * @return refresh токен привязанный к указанной сессии
     */
    String findRefreshTokenBySessionId(UUID sessionId);

    /**
     * Удаляет все access и refresh токены пользователя по id пользователя и сессии.
     * Тем самым закрывает указанную сессию.
     *
     * @param userId id пользователя
     * @param sessionId id сессии
     */
    void deleteAllTokensByUserIdAndSessionId(UUID userId, UUID sessionId);

    /**
     * Сохраняет refresh токен для указанных id пользователя и сессии на указанный срок.
     * Тем самым разрешает использование этого токена для авторизации.
     *
     * @param userId id пользователя
     * @param sessionId id сессии
     * @param token refresh токен
     * @param ttl время действия токена
     */
    void saveRefreshToken(UUID userId, UUID sessionId, String token, Duration ttl);

    /**
     * Сохраняет access токен для указанных id пользователя и сессии на указанный срок.
     * Тем самым разрешает использование этого токена для авторизации.
     *
     * @param userId id пользователя
     * @param sessionId id сессии
     * @param token access токен
     * @param ttl время действия токена
     */
    void saveAccessToken(UUID userId, UUID sessionId, String token, Duration ttl);

    /**
     * Удаляет все связанные access и refresh токены по id сессии, закрывая текущую сессию.
     *
     * @param sessionId id сессии
     */
    void deleteAllBySessionId(UUID sessionId);

    /**
     * Удаляет все связанные access и refresh токены по id пользователя, закрывая все открытые сессии на аккаунте.
     *
     * @param userId id пользователя
     */
    void deleteAllByUserId(UUID userId);
}
