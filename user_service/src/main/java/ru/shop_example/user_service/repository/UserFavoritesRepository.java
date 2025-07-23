package ru.shop_example.user_service.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.Update;
import ru.shop_example.user_service.entity.UserFavorites;

import java.util.UUID;

/**
 * Репозиторий для работы со списком избранного пользователя.
 * Представляет собой id пользователя как ключ и Set с id избранных товаров.
 *
 * @see UserFavorites
 */
public interface UserFavoritesRepository extends MongoRepository<UserFavorites, UUID>, UserFavoritesRepositoryCustom {

    /**
     * Удаление товара из набора избранных товаров пользователя.
     *
     * @param userId id пользователя
     * @param productId id товара
     */
    @Query(value = "{ '_id': ?0 }")
    @Update(value = "{ '$pull': { 'values': ?1 } }")
    void remove(UUID userId, UUID productId);

    /**
     * Очистка набора избранных товаров пользователя.
     *
     * @param UserId id пользователя
     */
    @Query(value = "{ '_id': ?0 }")
    @Update(value = "{ '$set': { 'values': [] } }")
    void clear(UUID UserId);
}
