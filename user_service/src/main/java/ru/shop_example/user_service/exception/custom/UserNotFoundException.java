package ru.shop_example.user_service.exception.custom;

/**
 * Пользователь не найден.
 */
public class UserNotFoundException extends RuntimeException{

    public UserNotFoundException(String message) {
        super(message);
    }
}
