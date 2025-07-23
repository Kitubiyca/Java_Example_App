package ru.shop_example.user_service.exception.custom;

/**
 * Ошибка валидации 4-х значного кода подтверждения. Коды не совпадают.
 *
 * @see OTPTimedOutException
 */
public class OTPException extends RuntimeException{

    public OTPException(String message) {
        super(message);
    }
}
