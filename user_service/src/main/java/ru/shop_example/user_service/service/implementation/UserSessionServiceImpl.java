package ru.shop_example.user_service.service.implementation;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import ru.shop_example.user_service.service.UserSessionService;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserSessionServiceImpl implements UserSessionService {

    private final StringRedisTemplate stringRedisTemplate;

    public void terminateCurrentSessionBySessionId(UUID sessionId){
        log.info("Called terminateCurrentSessionBySessionId service method");
        stringRedisTemplate.keys(String.format("token:*:*:%s", sessionId)).forEach(stringRedisTemplate::delete);
    }

    public void terminateAllSessionsByUserId(UUID userId){
        log.info("Called terminateAllSessionsByUserId service method");
        stringRedisTemplate.keys(String.format("token:*:%s:*", userId)).forEach(stringRedisTemplate::delete);
    }

}
