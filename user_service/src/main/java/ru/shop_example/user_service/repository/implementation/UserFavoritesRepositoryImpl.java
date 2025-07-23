package ru.shop_example.user_service.repository.implementation;

import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;
import ru.shop_example.user_service.entity.UserFavorites;
import ru.shop_example.user_service.repository.UserFavoritesRepository;
import ru.shop_example.user_service.repository.UserFavoritesRepositoryCustom;

import java.util.UUID;

/**
 * Реализация репозитория для работы со списком избранного пользователя (с расширением).
 * Представляет собой id пользователя как ключ и Set с id избранных товаров.
 *
 * @see UserFavoritesRepository
 * @see UserFavoritesRepositoryCustom
 */
@Repository
@RequiredArgsConstructor
public class UserFavoritesRepositoryImpl implements UserFavoritesRepositoryCustom {

    private final MongoTemplate mongoTemplate;

    /**
     * {@inheritDoc}
     */
    @Override
    public void add(UUID userId, UUID value) {
        Query query = Query.query(Criteria.where("_id").is(userId));
        Update update = new Update().addToSet("values", value).currentDate("lastChange");
        mongoTemplate.upsert(query, update, UserFavorites.class);
    }
}
