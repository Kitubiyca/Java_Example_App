package ru.shop_example.user_service.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

/**
 * Исходящее дто с access и refresh токенами для пользователя при авторизации.
 */
@Data
@AllArgsConstructor
@Builder
@Schema(description = "DTO с токенами доступа пользователя")
public class ResponseSignInDto {

    /** Access токен. */
    @Schema(description = "Access токен", example = "{token here}")
    private String accessToken;

    /** Refresh токен. */
    @Schema(description = "Refresh токен", example = "{token here}")
    private String refreshToken;
}
