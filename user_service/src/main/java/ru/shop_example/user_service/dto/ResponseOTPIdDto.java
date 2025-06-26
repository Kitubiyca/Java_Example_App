package ru.shop_example.user_service.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.util.UUID;

@Data
@AllArgsConstructor
@Schema(description = "DTO с новым id для кода подтверждения")
public class ResponseOTPIdDto {

    @Schema(description = "Значение UUID", example = "323e33e1-18f1-4bdf-b6b7-42db966c2229")
    private UUID value;
}
