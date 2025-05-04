package ru.shop_example.user_service.exception.custom;

public class InternalServerLogicException extends RuntimeException{

    public InternalServerLogicException(String message) {
        super(message);
    }
}
