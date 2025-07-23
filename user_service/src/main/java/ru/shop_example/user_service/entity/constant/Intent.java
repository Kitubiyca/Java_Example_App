package ru.shop_example.user_service.entity.constant;

import ru.shop_example.user_service.entity.OTP;
import ru.shop_example.user_service.entity.redis.RedisOTP;

/**
 * Цель создания кода подтверждения.
 * Используется для упрощения поиска в редисе.
 *
 * @see OTP
 * @see RedisOTP
 */
public enum Intent {

    /** Регистрация. */
    signUp,

    /** Авторизация. */
    signIn
}
