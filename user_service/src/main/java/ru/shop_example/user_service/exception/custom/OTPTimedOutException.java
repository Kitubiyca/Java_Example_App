package ru.shop_example.user_service.exception.custom;

public class OTPTimedOutException extends RuntimeException{

    public OTPTimedOutException(String message) {
        super(message);
    }
}
