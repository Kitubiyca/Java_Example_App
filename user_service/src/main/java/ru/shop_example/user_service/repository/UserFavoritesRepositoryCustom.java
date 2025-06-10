package ru.shop_example.user_service.repository;

import java.util.UUID;

public interface UserFavoritesRepositoryCustom {

    void add(UUID userId, UUID value);
}
