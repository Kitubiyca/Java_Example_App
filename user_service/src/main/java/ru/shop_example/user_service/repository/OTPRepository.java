package ru.shop_example.user_service.repository;

import ru.shop_example.user_service.entity.OTP;
import ru.shop_example.user_service.entity.constant.Intent;

import java.time.Duration;
import java.util.Optional;
import java.util.UUID;

/**
 * Репозиторий для работы с кодами подтверждения.
 *
 * @see OTP
 */
public interface OTPRepository {

    /**
     * Получение OTP по его id и назначению.
     *
     * @param intent Назначение OTP
     * @param id Id OTP
     *
     * @return Optional с или без найденного OTP.
     */
    Optional<OTP> getByIntentAndId(Intent intent, UUID id);

    /**
     * Сохранение OTP на определённое время (время его валидности).
     *
     * @param otp OTP
     * @param ttl срок валидности OTP
     */
    void set(OTP otp, Duration ttl);

    /**
     * Удаление OTP (он становится невалидным).
     *
     * @param id id OTP
     * @param intent назначение OTP
     */
    void deleteByIdAndIntent(UUID id, Intent intent);
}
