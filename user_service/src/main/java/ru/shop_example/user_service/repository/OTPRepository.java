package ru.shop_example.user_service.repository;

import ru.shop_example.user_service.entity.OTP;
import ru.shop_example.user_service.entity.constant.Intent;

import java.time.Duration;
import java.util.UUID;

public interface OTPRepository {
    OTP getByIntentAndId(Intent intent, UUID id);
    void set(OTP otp, Duration ttl);
    void deleteByIdAndIntent(UUID id, Intent intent);
}
