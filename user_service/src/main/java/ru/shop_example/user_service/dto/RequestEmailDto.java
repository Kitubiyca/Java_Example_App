package ru.shop_example.user_service.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

/**
 * Входящее дто с электронной почтой.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "DTO содержащее электронную почту")
public class RequestEmailDto {

    /** Электронная почта. */
    @Email(message = "email must follow email pattern")
    @NotBlank(message = "email must be not null or blank")
    @Schema(description = "Электронная почта", example = "example@example.com")
    private String email;
}
