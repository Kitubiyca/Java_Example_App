package ru.shop_example.user_service.exception.custom;

/**
 * Отказано в выполнении запроса пользователя. Не выполнены какие-то условия бизнес логики.
 */
public class RequestDeniedException extends RuntimeException{

    public RequestDeniedException(String message) {
        super(message);
    }
}
