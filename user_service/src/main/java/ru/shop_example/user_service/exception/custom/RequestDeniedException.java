package ru.shop_example.user_service.exception.custom;

public class RequestDeniedException extends RuntimeException{

    public RequestDeniedException(String message) {
        super(message);
    }
}
