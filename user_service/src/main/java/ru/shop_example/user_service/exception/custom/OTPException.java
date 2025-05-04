package ru.shop_example.user_service.exception.custom;

public class OTPException extends RuntimeException{

    public OTPException(String message) {
        super(message);
    }
}
