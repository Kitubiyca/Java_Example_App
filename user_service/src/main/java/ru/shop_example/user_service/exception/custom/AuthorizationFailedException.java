package ru.shop_example.user_service.exception.custom;

/**
 * Ошибка авторизации. Происходит при вооде неверных данных для авторизации пользователем.
 */
public class AuthorizationFailedException extends RuntimeException{

    public AuthorizationFailedException(String message) {
        super(message);
    }
}
