package ru.shop_example.user_service.exception.custom;

public class JWTValidationException extends RuntimeException{

    public JWTValidationException(String message) {
        super(message);
    }
}
