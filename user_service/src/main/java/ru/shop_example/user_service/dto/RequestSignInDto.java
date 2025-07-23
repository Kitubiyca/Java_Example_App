package ru.shop_example.user_service.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

/**
 * Входящее дто с электронной почто и паролем для авторизации.
 */
@Data
@NoArgsConstructor
@Schema(description = "DTO с данными для входа в аккаунт")
public class RequestSignInDto {

    /** Электронная почта. */
    @Email(message = "email must follow email pattern")
    @NotBlank(message = "email must be not null or blank")
    @Schema(description = "Электронная почта", example = "example@example.com")
    private String email;

    /** Пароль. */
    @NotBlank(message = "password must be not null or blank")
    @Schema(description = "Пароль", example = "1234")
    private String password;
}
