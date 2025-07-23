package ru.shop_example.user_service.entity.constant;

import ru.shop_example.user_service.entity.User;

/**
 * Текущий статус пользователя в БД.
 *
 * @see User
 */
public enum UserStatus {

    /** Ожидает окончания регистрации. */
    pending,

    /** Активный (стандартный статус). */
    active,

    /** Приостановленный (аккаунт временно не обслуживается). */
    suspended,

    /** Отмечен для удаления (Пользователем или администратором). */
    markedForDeletion

}
