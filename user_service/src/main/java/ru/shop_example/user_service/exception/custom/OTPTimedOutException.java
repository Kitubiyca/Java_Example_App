package ru.shop_example.user_service.exception.custom;

/**
 * Ошибка валидации 4-х значного кода подтверждения. Указанный id кода не найден (скорее всего истёк TTL).
 *
 * @see OTPException
 */
public class OTPTimedOutException extends RuntimeException{

    public OTPTimedOutException(String message) {
        super(message);
    }
}
