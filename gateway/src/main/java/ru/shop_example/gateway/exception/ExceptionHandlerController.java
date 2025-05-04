package ru.shop_example.gateway.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ServerWebExchange;
import ru.shop_example.gateway.dto.ErrorDto;
import ru.shop_example.gateway.exception.custom.JWTAuthorizationFailedException;
import ru.shop_example.gateway.exception.custom.UserRoleCheckException;

@RestControllerAdvice
@Slf4j
public class ExceptionHandlerController {

    @ResponseStatus(code = HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception.class)
    public ErrorDto handlerServerException (Exception exception, ServerWebExchange exchange){
        log.error(exception.getMessage());
        return new ErrorDto(
                HttpStatus.INTERNAL_SERVER_ERROR,
                HttpStatus.INTERNAL_SERVER_ERROR.toString(),
                exchange.getRequest().getURI().toString(),
                exception.getMessage()
        );
    }

    @ResponseStatus(code = HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(JWTAuthorizationFailedException.class)
    public ErrorDto jwtAuthorizationFailedException (JWTAuthorizationFailedException exception, ServerWebExchange exchange){
        log.error(exception.getMessage());
        return new ErrorDto(
                HttpStatus.UNAUTHORIZED,
                HttpStatus.UNAUTHORIZED.toString(),
                exchange.getRequest().getURI().toString(),
                exception.getMessage()
        );
    }

    @ResponseStatus(code = HttpStatus.FORBIDDEN)
    @ExceptionHandler(UserRoleCheckException.class)
    public ErrorDto userRoleCheckException (UserRoleCheckException exception, ServerWebExchange exchange){
        log.error(exception.getMessage());
        return new ErrorDto(
                HttpStatus.FORBIDDEN,
                HttpStatus.FORBIDDEN.toString(),
                exchange.getRequest().getURI().toString(),
                exception.getMessage()
        );
    }
}
