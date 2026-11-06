package ru.shop_example.user_service.dto;

import com.fasterxml.jackson.annotation.JsonAlias;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.*;

/**
 * Дто с булевой переменной для общения внутри приложения.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "DTO для получения логического значения с внутреннего сервиса")
public class IntegrationBooleanDto {

    /** Булево значение. */
    @Schema(description = "Логическое значение", example = "true")
    @NotNull(message = "booleanValue must not be null")
    @JsonAlias("value")
    private Boolean booleanValue;
}
