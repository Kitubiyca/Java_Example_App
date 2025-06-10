package ru.shop_example.user_service.repository;

import java.time.Duration;
import java.util.UUID;

public interface SessionTokenRepository {

    String findRefreshTokenBySessionId(UUID sessionId);
    void deleteAllTokensByUserIdAndSessionId(UUID userId, UUID sessionId);
    void saveRefreshToken(UUID userId, UUID sessionId, String token, Duration ttl);
    void saveAccessToken(UUID userId, UUID sessionId, String token, Duration ttl);
    void deleteAllBySessionId(UUID sessionId);
    void deleteAllByUserId(UUID userId);
}
