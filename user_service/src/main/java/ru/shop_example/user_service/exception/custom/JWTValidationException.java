package ru.shop_example.user_service.exception.custom;

/**
 * Ошибка Валидации JWT токена.
 */
public class JWTValidationException extends RuntimeException{

    public JWTValidationException(String message) {
        super(message);
    }
}
