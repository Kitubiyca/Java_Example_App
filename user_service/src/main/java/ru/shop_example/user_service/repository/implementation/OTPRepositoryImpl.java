package ru.shop_example.user_service.repository.implementation;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;
import ru.shop_example.user_service.entity.OTP;
import ru.shop_example.user_service.entity.constant.Intent;
import ru.shop_example.user_service.entity.redis.RedisOTP;
import ru.shop_example.user_service.repository.OTPRepository;

import java.time.Duration;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class OTPRepositoryImpl implements OTPRepository {

    private final RedisTemplate<String, RedisOTP> OTPRedisTemplate;

    private final String ID_FORMAT = "code:%s:%s";

    public OTP getByIntentAndId(Intent intent, UUID id){
        RedisOTP redisOtp = OTPRedisTemplate.opsForValue().get(String.format(ID_FORMAT, intent, id));
        return new OTP(id, redisOtp.getUserId(), intent, redisOtp.getValue());
    }

    public void set(OTP otp, Duration ttl){
        OTPRedisTemplate.opsForValue().set(
                String.format(ID_FORMAT, otp.getIntent(), otp.getId()),
                new RedisOTP(otp.getUserId(), otp.getValue()),
                ttl);
    }

    public void deleteByIdAndIntent(UUID id, Intent intent){
        OTPRedisTemplate.delete(String.format(ID_FORMAT, intent, id));
    }
}
