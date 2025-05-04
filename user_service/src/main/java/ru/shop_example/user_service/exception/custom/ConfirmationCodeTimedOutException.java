package ru.shop_example.user_service.exception.custom;

public class ConfirmationCodeTimedOutException extends RuntimeException{

    public ConfirmationCodeTimedOutException(String message) {
        super(message);
    }
}
