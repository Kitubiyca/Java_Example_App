package ru.shop_example.user_service.exception.custom;

/**
 * Сущность не найдена. Выбрасывается если не удаётся найти указанную пользователем сущность.
 * Необязательно вина пользователя, возможно сущность ещё не создана.
 */
public class EntityNotFoundException extends RuntimeException{

    public EntityNotFoundException(String message) {
        super(message);
    }
}
