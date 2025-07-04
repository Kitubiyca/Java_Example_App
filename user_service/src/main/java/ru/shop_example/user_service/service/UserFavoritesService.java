package ru.shop_example.user_service.service;

import ru.shop_example.user_service.dto.ResponseUserFavoritesDto;

import java.util.UUID;

public interface UserFavoritesService {

    ResponseUserFavoritesDto get(UUID userId);
    void add(UUID userId, UUID productId);
    void remove(UUID userId, UUID productId);
    void clear(UUID userId);
}
