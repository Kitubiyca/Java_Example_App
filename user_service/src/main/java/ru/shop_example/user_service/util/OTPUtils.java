package ru.shop_example.user_service.util;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.shop_example.user_service.entity.OTP;
import ru.shop_example.user_service.entity.constant.Intent;

import java.security.SecureRandom;
import java.util.UUID;

/**
 * Утилиты для работы с OTP.
 *
 * @see OTP
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class OTPUtils {

    private final SecureRandom secureRandom = new SecureRandom();

    /**
     * Создание нового 4-х значного кода подтверждения.
     *
     * @param userId id пользователя
     * @param intent назначение кода
     *
     * @return сущность OTP (временного кода подтверждения)
     */
    public OTP createOTP (UUID userId, Intent intent){
        return new OTP(UUID.randomUUID(), userId, intent, String.format("%04d", secureRandom.nextInt(10000)));
    }
}
