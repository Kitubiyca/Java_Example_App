package ru.shop_example.user_service.entity.redis;

import lombok.*;

import java.util.UUID;

/**
 * Код подтверждения, хранящийся в редисе.
 */
@Getter
@Setter(AccessLevel.PUBLIC)
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class RedisOTP {

    /** Id пользователя. */
    private UUID userId;

    /** Код подтверждения. */
    private String value;
}
