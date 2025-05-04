package ru.shop_example.user_service.exception.custom;

public class AuthorizationFailedException extends RuntimeException{

    public AuthorizationFailedException(String message) {
        super(message);
    }
}
