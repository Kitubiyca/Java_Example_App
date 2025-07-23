package ru.shop_example.user_service.repository;

import java.util.UUID;

/**
 * Расширение для репозитория списка избранного пользователя.
 *
 * @see UserFavoritesRepository
 */
public interface UserFavoritesRepositoryCustom {

    /**
     * Добавляет id товара в список избранного пользователя по его id.
     * Если списка ещё нет в бд, создаёт его.
     *
     * @param userId id пользователя
     * @param value id продукта
     */
    void add(UUID userId, UUID value);
}
