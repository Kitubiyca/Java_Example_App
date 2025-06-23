package ru.shop_example.user_service.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@Schema(description = "DTO содержащее refresh токен пользователя")
public class RequestRefreshTokenDto {

    @NotBlank
    @Schema(description = "Токен", example = "{token here}")
    private String value;
}
