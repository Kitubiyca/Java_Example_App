package ru.shop_example.user_service.dto.redis;

import lombok.*;

import java.util.UUID;

@Getter
@Setter(AccessLevel.PUBLIC)
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class RedisConfirmationCodeDto {

    private UUID userId;
    private String value;
}
