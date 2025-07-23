package ru.shop_example.user_service.exception;


import io.swagger.v3.oas.annotations.Hidden;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ru.shop_example.user_service.dto.ResponseErrorDto;
import ru.shop_example.user_service.exception.custom.*;

/**
 * Глобальный обработчик ошибок.
 * Возвращает в теле ответа JSON с информацией об ошибке.
 *
 * @see ResponseErrorDto
 */
@ControllerAdvice
@Slf4j
@RestController
@Hidden
public class ExceptionHandlerController {

    @ResponseStatus(code = HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception.class)
    public ResponseErrorDto serverExceptionHandler(Exception exception, HttpServletRequest request) {
        log.error(exception.getMessage());
        exception.getStackTrace();
        return new ResponseErrorDto(
                HttpStatus.INTERNAL_SERVER_ERROR,
                HttpStatus.INTERNAL_SERVER_ERROR.toString(),
                request.getRequestURI(),
                exception.getMessage());
    }

    @ResponseStatus(code = HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseErrorDto methodArgumentNotValidExceptionHandler(MethodArgumentNotValidException exception, HttpServletRequest request) {
        log.error(exception.getMessage());
        return new ResponseErrorDto(
                HttpStatus.BAD_REQUEST,
                HttpStatus.BAD_REQUEST.toString(),
                request.getRequestURI(),
                exception.getMessage());
    }

    @ResponseStatus(code = HttpStatus.BAD_REQUEST)
    @ExceptionHandler(BindException.class)
    public ResponseErrorDto bindExceptionHandler(BindException exception, HttpServletRequest request) {
        log.error(exception.getMessage());
        return new ResponseErrorDto(
                HttpStatus.BAD_REQUEST,
                HttpStatus.BAD_REQUEST.toString(),
                request.getRequestURI(),
                exception.getMessage());
    }

    @ResponseStatus(code = HttpStatus.BAD_REQUEST)
    @ExceptionHandler(UserAlreadyExistsException.class)
    public ResponseErrorDto userAlreadyExistsExceptionHandler(UserAlreadyExistsException exception, HttpServletRequest request) {
        log.error(exception.getMessage());
        return new ResponseErrorDto(
                HttpStatus.BAD_REQUEST,
                HttpStatus.BAD_REQUEST.toString(),
                request.getRequestURI(),
                exception.getMessage());
    }

    @ResponseStatus(code = HttpStatus.NOT_FOUND)
    @ExceptionHandler(UserNotFoundException.class)
    public ResponseErrorDto userNotFoundExceptionHandler(UserNotFoundException exception, HttpServletRequest request) {
        log.error(exception.getMessage());
        return new ResponseErrorDto(
                HttpStatus.NOT_FOUND,
                HttpStatus.NOT_FOUND.toString(),
                request.getRequestURI(),
                exception.getMessage());
    }

    @ResponseStatus(code = HttpStatus.NOT_FOUND)
    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseErrorDto entityNotFoundExceptionHandler(EntityNotFoundException exception, HttpServletRequest request) {
        log.error(exception.getMessage());
        return new ResponseErrorDto(
                HttpStatus.NOT_FOUND,
                HttpStatus.NOT_FOUND.toString(),
                request.getRequestURI(),
                exception.getMessage());
    }

    @ResponseStatus(code = HttpStatus.BAD_REQUEST)
    @ExceptionHandler(RequestDeniedException.class)
    public ResponseErrorDto requestDeniedExceptionHandler(RequestDeniedException exception, HttpServletRequest request) {
        log.error(exception.getMessage());
        return new ResponseErrorDto(
                HttpStatus.BAD_REQUEST,
                HttpStatus.BAD_REQUEST.toString(),
                request.getRequestURI(),
                exception.getMessage());
    }

    @ResponseStatus(code = HttpStatus.BAD_REQUEST)
    @ExceptionHandler(OTPTimedOutException.class)
    public ResponseErrorDto OTPTimedOutExceptionHandler(OTPTimedOutException exception, HttpServletRequest request) {
        log.error(exception.getMessage());
        return new ResponseErrorDto(
                HttpStatus.BAD_REQUEST,
                HttpStatus.BAD_REQUEST.toString(),
                request.getRequestURI(),
                exception.getMessage());
    }

    @ResponseStatus(code = HttpStatus.BAD_REQUEST)
    @ExceptionHandler(OTPException.class)
    public ResponseErrorDto otpExceptionHandler(OTPException exception, HttpServletRequest request) {
        log.error(exception.getMessage());
        return new ResponseErrorDto(
                HttpStatus.BAD_REQUEST,
                HttpStatus.BAD_REQUEST.toString(),
                request.getRequestURI(),
                exception.getMessage());
    }

    @ResponseStatus(code = HttpStatus.BAD_REQUEST)
    @ExceptionHandler(JWTValidationException.class)
    public ResponseErrorDto jwtValidationExceptionHandler(JWTValidationException exception, HttpServletRequest request) {
        log.error(exception.getMessage());
        return new ResponseErrorDto(
                HttpStatus.BAD_REQUEST,
                HttpStatus.BAD_REQUEST.toString(),
                request.getRequestURI(),
                exception.getMessage());
    }

    @ResponseStatus(code = HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(AuthorizationFailedException.class)
    public ResponseErrorDto authorizationFailedExceptionHandler(AuthorizationFailedException exception, HttpServletRequest request) {
        log.error(exception.getMessage());
        return new ResponseErrorDto(
                HttpStatus.UNAUTHORIZED,
                HttpStatus.UNAUTHORIZED.toString(),
                request.getRequestURI(),
                exception.getMessage());
    }
}
