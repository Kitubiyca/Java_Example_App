package ru.shop_example.user_service.repository.implementation;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Repository;
import ru.shop_example.user_service.repository.SessionTokenRepository;

import java.time.Duration;
import java.util.UUID;

/**
 * Репозиторий для работы с токенами авторизации.
 * Использует Redis и StringRedisTemplate.
 */
@Repository
@RequiredArgsConstructor
public class SessionTokenRepositoryImpl implements SessionTokenRepository {

    private final StringRedisTemplate stringRedisTemplate;

    /**
     * {@inheritDoc}
     */
    public String findRefreshTokenBySessionId(UUID sessionId){
        return stringRedisTemplate.keys(String.format("token:refresh:*:%s", sessionId)).iterator().next();
    }

    /**
     * {@inheritDoc}
     */
    public void deleteAllTokensByUserIdAndSessionId(UUID userId, UUID sessionId){
        stringRedisTemplate.keys(String.format("token:*:%s:%s", userId, sessionId)).forEach(stringRedisTemplate::delete);
    }

    /**
     * {@inheritDoc}
     */
    public void saveRefreshToken(UUID userId, UUID sessionId, String token, Duration ttl){
        stringRedisTemplate.opsForValue().set(String.format("token:refresh:%s:%s", userId, sessionId), token, ttl);
    }

    /**
     * {@inheritDoc}
     */
    public void saveAccessToken(UUID userId, UUID sessionId, String token, Duration ttl){
        stringRedisTemplate.opsForValue().set(String.format("token:access:%s:%s", userId, sessionId), token, ttl);
    }

    /**
     * {@inheritDoc}
     */
    public void deleteAllBySessionId(UUID sessionId){
        stringRedisTemplate.keys(String.format("token:*:*:%s", sessionId)).forEach(stringRedisTemplate::delete);
    }

    /**
     * {@inheritDoc}
     */
    public void deleteAllByUserId(UUID userId){
        stringRedisTemplate.keys(String.format("token:*:%s:*", userId)).forEach(stringRedisTemplate::delete);
    }
}
