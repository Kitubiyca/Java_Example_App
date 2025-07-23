package ru.shop_example.user_service.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

/**
 * Входящее дто с refresh токеном.
 */
@Data
@NoArgsConstructor
@Schema(description = "DTO содержащее refresh токен пользователя")
public class RequestRefreshTokenDto {

    /** Refresh токен. */
    @NotBlank(message = "refreshToken must be not null or blank")
    @Schema(description = "Токен", example = "{token here}")
    private String refreshToken;
}
