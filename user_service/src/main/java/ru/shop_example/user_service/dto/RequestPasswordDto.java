package ru.shop_example.user_service.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "DTO содержащее пароль пользователя")
public class RequestPasswordDto {

    @NotBlank
    @Schema(description = "Пароль", example = "1234")
    public String password;
}
