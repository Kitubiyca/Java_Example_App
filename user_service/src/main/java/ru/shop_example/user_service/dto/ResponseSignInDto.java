package ru.shop_example.user_service.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@Builder
@Schema(description = "DTO с токенами доступа пользователя")
public class ResponseSignInDto {

    @Schema(description = "Access токен", example = "{token here}")
    private String accessToken;
    @Schema(description = "Refresh токен", example = "{token here}")
    private String refreshToken;
}
