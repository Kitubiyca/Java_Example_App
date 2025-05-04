package ru.shop_example.user_service.service;

import java.util.UUID;

public interface UserSessionService {
    void terminateCurrentSessionBySessionId(UUID sessionId);
    void terminateAllSessionsByUserId(UUID userId);
}
