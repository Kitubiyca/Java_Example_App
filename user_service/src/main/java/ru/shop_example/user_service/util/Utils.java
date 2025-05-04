package ru.shop_example.user_service.util;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import ru.shop_example.user_service.dto.kafka.KafkaConfirmationCodeDto;
import ru.shop_example.user_service.dto.redis.RedisConfirmationCodeDto;
import ru.shop_example.user_service.entity.User;
import ru.shop_example.user_service.entity.constant.Intent;
import ru.shop_example.user_service.service.kafka.EmailNotificationSender;

import java.security.SecureRandom;
import java.time.Duration;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class Utils {

    private final RedisTemplate<String, RedisConfirmationCodeDto> redisConfirmationCodeDtoRedisTemplate;
    private final EmailNotificationSender emailNotificationSender;
    private final SecureRandom secureRandom = new SecureRandom();

    public UUID sendConfirmationCodeWithEmail(User user, Intent intent, Duration ttl){
        log.info("Called sendConfirmationCodeWithEmail utils method");
        String stringCode = String.format("%04d", secureRandom.nextInt(10000));
        UUID confirmationId = UUID.randomUUID();
        redisConfirmationCodeDtoRedisTemplate.opsForValue().set(
                String.format("code:%s:%s", intent, confirmationId),
                new RedisConfirmationCodeDto(user.getId(), stringCode),
                ttl);
        emailNotificationSender.sendToEmailConfirmationCodeTopic(new KafkaConfirmationCodeDto(user.getFirstname(), user.getEmail(), stringCode));
        return confirmationId;
    }
}
