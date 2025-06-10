package ru.shop_example.user_service.entity.redis;

import lombok.*;

import java.util.UUID;

@Getter
@Setter(AccessLevel.PUBLIC)
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class RedisOTP {

    private UUID userId;
    private String value;
}
