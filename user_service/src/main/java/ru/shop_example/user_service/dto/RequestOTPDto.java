package ru.shop_example.user_service.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.UUID;

/**
 * Входящее дто с кодом подтверждения.
 */
@Data
@NoArgsConstructor
@Schema(description = "DTO содержащее одноразовый код подтверждения")
public class RequestOTPDto {

    /** Id кода подтверждения */
    @NotNull
    @Schema(description = "Идентификатор пользователя", example = "323e33e1-18f1-4bdf-b6b7-42db966c2229")
    private UUID id;
    /** Код подтверждения 4 символа. */

    @NotBlank
    @Size(min = 4, max = 4)
    @Schema(description = "Код подтверждения", example = "0000")
    private String value;
}
