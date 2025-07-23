package ru.shop_example.user_service.entity;

import lombok.*;
import ru.shop_example.user_service.entity.constant.Intent;

import java.util.UUID;

/**
 * Временный код подтверждения.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class OTP {

    /** Идентификатор кода. */
    private UUID id;

    /** Идентификатор пользователя. */
    private UUID userId;

    /** Назначение кода. */
    private Intent intent;

    /** Код подтверждения (4 цифры). */
    private String value;
}
