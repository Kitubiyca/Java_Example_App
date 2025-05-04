package ru.shop_example.user_service.dto;

import lombok.*;

@Getter
@Setter(AccessLevel.PUBLIC)
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class SignInResponseDto {

    String accessToken;
    String refreshToken;
}
