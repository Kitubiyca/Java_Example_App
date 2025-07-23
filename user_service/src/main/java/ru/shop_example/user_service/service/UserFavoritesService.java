package ru.shop_example.user_service.service;

import ru.shop_example.user_service.dto.ResponseUserFavoritesDto;
import ru.shop_example.user_service.controller.UserFavoritesController;
import ru.shop_example.user_service.entity.UserFavorites;

import java.util.UUID;

/**
 * Сервис для работы со списком избранного пользователя.
 *
 * @see UserFavorites
 * @see UserFavoritesController
 */
public interface UserFavoritesService {

    /**
     * Получить список избранного.
     *
     * @param userId id пользователя
     *
     * @return дто со списком избранного
     */
    ResponseUserFavoritesDto get(UUID userId);

    /**
     * Добавить товар в список избранного.
     *
     * @param userId id пользователя
     * @param productId id товара
     */
    void add(UUID userId, UUID productId);

    /**
     * Удалить товар из списка избранного.
     *
     * @param userId id пользователя
     * @param productId id товара
     */
    void remove(UUID userId, UUID productId);

    /**
     * Очистить список избранного.
     *
     * @param userId id пользователя
     */
    void clear(UUID userId);
}
