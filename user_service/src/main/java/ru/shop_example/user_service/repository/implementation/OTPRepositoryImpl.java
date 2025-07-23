package ru.shop_example.user_service.repository.implementation;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;
import ru.shop_example.user_service.entity.OTP;
import ru.shop_example.user_service.entity.constant.Intent;
import ru.shop_example.user_service.entity.redis.RedisOTP;
import ru.shop_example.user_service.repository.OTPRepository;
import ru.shop_example.user_service.configuration.RedisTemplateConfiguration;

import java.time.Duration;
import java.util.Optional;
import java.util.UUID;

/**
 * Репозиторий для работы с кодами подтверждения.
 * Работает с Redis с помощью RedisTemplate.
 *
 * @see OTPRepository
 * @see RedisTemplateConfiguration
 */
@Repository
@RequiredArgsConstructor
public class OTPRepositoryImpl implements OTPRepository {

    private final RedisTemplate<String, RedisOTP> OTPRedisTemplate;

    private final String ID_FORMAT = "code:%s:%s";

    /**
     * {@inheritDoc}
     * get() возвращает null, поэтому результат проверяется и оборачивается в Optional.
     */
    public Optional<OTP> getByIntentAndId(Intent intent, UUID id){
        RedisOTP redisOtp = OTPRedisTemplate.opsForValue().get(String.format(ID_FORMAT, intent, id));
        if (redisOtp == null) return Optional.empty();
        return Optional.of(new OTP(id, redisOtp.getUserId(), intent, redisOtp.getValue()));
    }

    /**
     * {@inheritDoc}
     */
    public void set(OTP otp, Duration ttl){
        OTPRedisTemplate.opsForValue().set(
                String.format(ID_FORMAT, otp.getIntent(), otp.getId()),
                new RedisOTP(otp.getUserId(), otp.getValue()),
                ttl);
    }

    /**
     * {@inheritDoc}
     */
    public void deleteByIdAndIntent(UUID id, Intent intent){
        OTPRedisTemplate.delete(String.format(ID_FORMAT, intent, id));
    }
}
