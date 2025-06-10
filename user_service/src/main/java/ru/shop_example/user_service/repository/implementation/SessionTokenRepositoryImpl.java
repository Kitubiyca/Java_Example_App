package ru.shop_example.user_service.repository.implementation;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Repository;
import ru.shop_example.user_service.repository.SessionTokenRepository;

import java.time.Duration;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class SessionTokenRepositoryImpl implements SessionTokenRepository {

    private final StringRedisTemplate stringRedisTemplate;

    public String findRefreshTokenBySessionId(UUID sessionId){
        return stringRedisTemplate.keys(String.format("token:refresh:*:%s", sessionId)).iterator().next();
    }

    public void deleteAllTokensByUserIdAndSessionId(UUID userId, UUID sessionId){
        stringRedisTemplate.keys(String.format("token:*:%s:%s", userId, sessionId)).forEach(stringRedisTemplate::delete);
    }

    public void saveRefreshToken(UUID userId, UUID sessionId, String token, Duration ttl){
        stringRedisTemplate.opsForValue().set(String.format("token:refresh:%s:%s", userId, sessionId), token, ttl);
    }

    public void saveAccessToken(UUID userId, UUID sessionId, String token, Duration ttl){
        stringRedisTemplate.opsForValue().set(String.format("token:access:%s:%s", userId, sessionId), token, ttl);
    }

    public void deleteAllBySessionId(UUID sessionId){
        stringRedisTemplate.keys(String.format("token:*:*:%s", sessionId)).forEach(stringRedisTemplate::delete);
    }

    public void deleteAllByUserId(UUID userId){
        stringRedisTemplate.keys(String.format("token:*:%s:*", userId)).forEach(stringRedisTemplate::delete);
    }
}
