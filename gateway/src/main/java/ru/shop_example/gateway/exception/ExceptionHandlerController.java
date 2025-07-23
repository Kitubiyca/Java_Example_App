package ru.shop_example.gateway.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.server.ServerWebExchange;
import ru.shop_example.gateway.dto.ErrorDto;
import ru.shop_example.gateway.exception.custom.JWTAuthorizationFailedException;
import ru.shop_example.gateway.exception.custom.UserRoleCheckException;

@RestControllerAdvice
@Slf4j
public class ExceptionHandlerController {

    @ResponseStatus(code = HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception.class)
    public ErrorDto serverExceptionHandler(Exception exception, ServerWebExchange exchange) {
        log.error(exception.getMessage());
        log.error(exception.getClass().getName());
        return new ErrorDto(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(),
                exchange.getRequest().getURI().toString(),
                exception.getMessage()
        );
    }

    @ExceptionHandler(WebClientResponseException.class)
    public ResponseEntity<ErrorDto> webClientResponseExceptionExceptionHandler(WebClientResponseException exception) {
        log.error(exception.getMessage());
        return ResponseEntity.status(exception.getStatusCode()).body(exception.getResponseBodyAs(ErrorDto.class));
    }

    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<ErrorDto> notFoundExceptionHandler(ResponseStatusException exception, ServerWebExchange exchange) {
        log.error(exception.getMessage());
        HttpStatus httpStatus = HttpStatus.valueOf(exception.getStatusCode().value());
        return ResponseEntity.status(exception.getStatusCode()).body(
                new ErrorDto(
                        httpStatus.value(),
                        httpStatus.getReasonPhrase(),
                        exchange.getRequest().getURI().toString(),
                        exception.getMessage()
                ));
    }

    @ResponseStatus(code = HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(JWTAuthorizationFailedException.class)
    public ErrorDto jwtAuthorizationFailedExceptionHandler(JWTAuthorizationFailedException exception, ServerWebExchange exchange) {
        log.error(exception.getMessage());
        return new ErrorDto(
                HttpStatus.UNAUTHORIZED.value(),
                HttpStatus.UNAUTHORIZED.getReasonPhrase(),
                exchange.getRequest().getURI().toString(),
                exception.getMessage()
        );
    }

    @ResponseStatus(code = HttpStatus.FORBIDDEN)
    @ExceptionHandler(UserRoleCheckException.class)
    public ErrorDto userRoleCheckExceptionHandler(UserRoleCheckException exception, ServerWebExchange exchange) {
        log.error(exception.getMessage());
        return new ErrorDto(
                HttpStatus.FORBIDDEN.value(),
                HttpStatus.FORBIDDEN.getReasonPhrase(),
                exchange.getRequest().getURI().toString(),
                exception.getMessage()
        );
    }
}
