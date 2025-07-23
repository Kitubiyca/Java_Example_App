package ru.shop_example.user_service.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import ru.shop_example.user_service.exception.ExceptionHandlerController;

import java.time.Instant;

/**
 * Исходящее дто с информацией об ошибке во время запроса.
 *
 * @see ExceptionHandlerController
 */
@Data
@AllArgsConstructor
@Builder
@Schema(description = "DTO содержащее ошибку")
public class ResponseErrorDto {

    /** Код ошибки формата "400". */
    @Schema(description = "Код ошибки", example = "400")
    private int errorCode;

    /** Текстовая расшифровка ошибки формата "BAD_REQUEST". */
    @Schema(description = "Расшифровка ошибки", example = "BAD_REQUEST")
    private String errorText;

    /** Запрос, который вернул ошибку, формата "sign-up/request". */
    @Schema(description = "Запрос, вызвавший ошибку", example = "sign-up/request")
    private String url;

    /** Сообщение от сервера, формата "Validation failed for argument...". */
    @Schema(description = "Сообщение от сервера", example = "Validation failed for argument...")
    private String message;

    /** Временная метка, когда произошла ошибка, формата "2025-06-17T15:31:41.221527506". */
    @Schema(description = "Временная метка", example = "2025-06-17T15:31:41.221527506")
    private final Instant timeStamp = Instant.now();;
}
