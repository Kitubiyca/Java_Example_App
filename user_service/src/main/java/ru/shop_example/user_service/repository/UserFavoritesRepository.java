package ru.shop_example.user_service.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.Update;
import ru.shop_example.user_service.entity.UserFavorites;

import java.util.UUID;

public interface UserFavoritesRepository extends MongoRepository<UserFavorites, UUID>, UserFavoritesRepositoryCustom {

    @Query(value = "{ '_id': ?0 }")
    @Update(value = "{ '$pull': { 'values': ?1 } }")
    void remove(UUID userId, UUID productId);

    @Query(value = "{ '_id': ?0 }")
    @Update(value = "{ '$set': { 'values': [] } }")
    void clear(UUID UserId);
}
