package ru.shop_example.user_service.entity;

import lombok.*;
import ru.shop_example.user_service.entity.constant.Intent;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class OTP {

    private UUID id;
    private UUID userId;
    private Intent intent;
    private String value;
}
