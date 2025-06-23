package ru.shop_example.user_service.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;


@Getter
@Setter
@NoArgsConstructor
@Schema(description = "DTO содержащее электронную почту")
public class RequestEmailDto {

    @Email
    @NotBlank
    @Schema(description = "Электронная почта", example = "example@example.com")
    private String email;
}
