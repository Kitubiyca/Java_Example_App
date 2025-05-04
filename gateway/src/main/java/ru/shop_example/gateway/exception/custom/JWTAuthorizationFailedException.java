package ru.shop_example.gateway.exception.custom;

public class JWTAuthorizationFailedException extends RuntimeException{

    public JWTAuthorizationFailedException(String message) {
        super(message);
    }
}
