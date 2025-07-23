package ru.shop_example.user_service.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.shop_example.user_service.dto.ResponseUserFavoritesDto;
import ru.shop_example.user_service.entity.UserFavorites;

/**
 * Маппер списка избранного пользователя.
 *
 * @see UserFavorites
 */
@Mapper(componentModel = "spring")
public interface UserFavoritesMapper {

    @Mapping(source = "values", target = "favorites")
    ResponseUserFavoritesDto userFavoritesToUserFavoritesDto(UserFavorites userFavorites);
}
