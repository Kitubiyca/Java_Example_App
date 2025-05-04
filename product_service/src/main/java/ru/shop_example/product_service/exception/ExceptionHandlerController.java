package ru.shop_example.product_service.exception;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.ws.rs.NotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ru.shop_example.product_service.dto.ErrorDto;

@ControllerAdvice
@Slf4j
@RestController
public class ExceptionHandlerController {

    @ResponseStatus(code = HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception.class)
    public ErrorDto serverExceptionHandler(Exception exception, HttpServletRequest request) {
        log.error(exception.getMessage());
        return new ErrorDto(
                HttpStatus.INTERNAL_SERVER_ERROR,
                HttpStatus.INTERNAL_SERVER_ERROR.toString(),
                request.getRequestURI(),
                exception.getMessage());
    }

    @ResponseStatus(code = HttpStatus.NOT_FOUND)
    @ExceptionHandler(NotFoundException.class)
    public ErrorDto notFoundExceptionHandler(NotFoundException exception, HttpServletRequest request) {
        log.error(exception.getMessage());
        return new ErrorDto(
                HttpStatus.NOT_FOUND,
                HttpStatus.NOT_FOUND.toString(),
                request.getRequestURI(),
                exception.getMessage());
    }
}
