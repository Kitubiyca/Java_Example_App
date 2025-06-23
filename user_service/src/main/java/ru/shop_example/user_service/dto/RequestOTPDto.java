package ru.shop_example.user_service.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@Schema(description = "DTO содержащее одноразовый код подтверждения")
public class RequestOTPDto {

    @NotNull
    @Schema(description = "Идентификатор пользователя", example = "323e33e1-18f1-4bdf-b6b7-42db966c2229")
    private UUID id;
    @NotBlank
    @Schema(description = "Код подтверждения", example = "0000")
    private String value;
}
