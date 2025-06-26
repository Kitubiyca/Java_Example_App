package ru.shop_example.user_service.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "DTO для получения логического значения с внутреннего сервиса")
public class IntegrationBooleanDto {

    @Schema(description = "Логическое начение", example = "true")
    @NotNull
    private Boolean value;
}
