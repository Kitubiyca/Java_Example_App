package ru.shop_example.user_service.dto;

import lombok.*;

import java.util.UUID;

@Getter
@Setter(AccessLevel.PUBLIC)
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class OTPIdResponseDto {

    private UUID value;
}
