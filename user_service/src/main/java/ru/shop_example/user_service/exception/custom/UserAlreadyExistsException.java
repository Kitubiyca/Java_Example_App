package ru.shop_example.user_service.exception.custom;

/**
 * Ошибка регистрации пользователя. Пользователь с такими уникальными данными уже зарегестрирован.
 */
public class UserAlreadyExistsException extends RuntimeException{

    public UserAlreadyExistsException(String message) {
        super(message);
    }
}
