package ru.shop_example.user_service.repository.implementation;

import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;
import ru.shop_example.user_service.entity.UserFavorites;
import ru.shop_example.user_service.repository.UserFavoritesRepositoryCustom;

import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class UserFavoritesRepositoryImpl implements UserFavoritesRepositoryCustom {

    private final MongoTemplate mongoTemplate;

    @Override
    public void add(UUID userId, UUID value) {
        Query query = Query.query(Criteria.where("_id").is(userId));
        Update update = new Update().addToSet("values", value).currentDate("lastChange");
        mongoTemplate.upsert(query, update, UserFavorites.class);
    }
}
