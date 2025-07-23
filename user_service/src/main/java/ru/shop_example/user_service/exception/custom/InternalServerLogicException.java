package ru.shop_example.user_service.exception.custom;

/**
 * Ошибка сервера. Не должна происходить при нормальных обстоятельствах.
 */
public class InternalServerLogicException extends RuntimeException{

    public InternalServerLogicException(String message) {
        super(message);
    }
}
