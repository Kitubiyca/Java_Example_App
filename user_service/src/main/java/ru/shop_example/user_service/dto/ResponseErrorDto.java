package ru.shop_example.user_service.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import org.springframework.http.HttpStatus;

import java.time.Instant;

@Data
@AllArgsConstructor
@Builder
@Schema(description = "DTO содержащее ошибку")
public class ResponseErrorDto {

    @Schema(description = "Код ошибки", example = "BAD_REQUEST")
    private HttpStatus errorCode;
    @Schema(description = "Расшифровка ошибки", example = "400 BAD_REQUEST")
    private String errorText;
    @Schema(description = "Запрос, вызвавший ошибку", example = "sign-up/request")
    private String url;
    @Schema(description = "Сообщение от сервера", example = "Validation failed for argument...")
    private String message;
    @Schema(description = "Временная метка", example = "2025-06-17T15:31:41.221527506")
    private final Instant timeStamp = Instant.now();;
}
