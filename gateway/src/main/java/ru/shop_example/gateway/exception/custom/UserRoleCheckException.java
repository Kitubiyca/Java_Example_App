package ru.shop_example.gateway.exception.custom;

public class UserRoleCheckException extends RuntimeException{

    public UserRoleCheckException(String message) {
        super(message);
    }
}
