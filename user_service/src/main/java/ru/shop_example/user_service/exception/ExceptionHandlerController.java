package ru.shop_example.user_service.exception;


import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ru.shop_example.user_service.dto.ErrorDto;
import ru.shop_example.user_service.exception.custom.*;

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

    @ResponseStatus(code = HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ErrorDto methodArgumentNotValidExceptionHandler(MethodArgumentNotValidException exception, HttpServletRequest request) {
        log.error(exception.getMessage());
        return new ErrorDto(
                HttpStatus.BAD_REQUEST,
                HttpStatus.BAD_REQUEST.toString(),
                request.getRequestURI(),
                exception.getMessage());
    }

    @ResponseStatus(code = HttpStatus.BAD_REQUEST)
    @ExceptionHandler(BindException.class)
    public ErrorDto bindExceptionHandler(BindException exception, HttpServletRequest request) {
        log.error(exception.getMessage());
        return new ErrorDto(
                HttpStatus.BAD_REQUEST,
                HttpStatus.BAD_REQUEST.toString(),
                request.getRequestURI(),
                exception.getMessage());
    }

    @ResponseStatus(code = HttpStatus.BAD_REQUEST)
    @ExceptionHandler(UserAlreadyExistsException.class)
    public ErrorDto userAlreadyExistsExceptionHandler(UserAlreadyExistsException exception, HttpServletRequest request) {
        log.error(exception.getMessage());
        return new ErrorDto(
                HttpStatus.BAD_REQUEST,
                HttpStatus.BAD_REQUEST.toString(),
                request.getRequestURI(),
                exception.getMessage());
    }

    @ResponseStatus(code = HttpStatus.NOT_FOUND)
    @ExceptionHandler(UserNotFoundException.class)
    public ErrorDto userNotFoundExceptionHandler(UserNotFoundException exception, HttpServletRequest request) {
        log.error(exception.getMessage());
        return new ErrorDto(
                HttpStatus.NOT_FOUND,
                HttpStatus.NOT_FOUND.toString(),
                request.getRequestURI(),
                exception.getMessage());
    }

    @ResponseStatus(code = HttpStatus.BAD_REQUEST)
    @ExceptionHandler(RequestDeniedException.class)
    public ErrorDto requestDeniedExceptionHandler(RequestDeniedException exception, HttpServletRequest request) {
        log.error(exception.getMessage());
        return new ErrorDto(
                HttpStatus.BAD_REQUEST,
                HttpStatus.BAD_REQUEST.toString(),
                request.getRequestURI(),
                exception.getMessage());
    }

    @ResponseStatus(code = HttpStatus.BAD_REQUEST)
    @ExceptionHandler(OTPTimedOutException.class)
    public ErrorDto OTPTimedOutExceptionHandler(OTPTimedOutException exception, HttpServletRequest request) {
        log.error(exception.getMessage());
        return new ErrorDto(
                HttpStatus.BAD_REQUEST,
                HttpStatus.BAD_REQUEST.toString(),
                request.getRequestURI(),
                exception.getMessage());
    }

    @ResponseStatus(code = HttpStatus.BAD_REQUEST)
    @ExceptionHandler(OTPException.class)
    public ErrorDto otpExceptionHandler(OTPException exception, HttpServletRequest request) {
        log.error(exception.getMessage());
        return new ErrorDto(
                HttpStatus.BAD_REQUEST,
                HttpStatus.BAD_REQUEST.toString(),
                request.getRequestURI(),
                exception.getMessage());
    }

    @ResponseStatus(code = HttpStatus.BAD_REQUEST)
    @ExceptionHandler(JWTValidationException.class)
    public ErrorDto jwtValidationExceptionHandler(JWTValidationException exception, HttpServletRequest request) {
        log.error(exception.getMessage());
        return new ErrorDto(
                HttpStatus.BAD_REQUEST,
                HttpStatus.BAD_REQUEST.toString(),
                request.getRequestURI(),
                exception.getMessage());
    }

    @ResponseStatus(code = HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(AuthorizationFailedException.class)
    public ErrorDto authorizationFailedExceptionHandler(AuthorizationFailedException exception, HttpServletRequest request) {
        log.error(exception.getMessage());
        return new ErrorDto(
                HttpStatus.UNAUTHORIZED,
                HttpStatus.UNAUTHORIZED.toString(),
                request.getRequestURI(),
                exception.getMessage());
    }
}
