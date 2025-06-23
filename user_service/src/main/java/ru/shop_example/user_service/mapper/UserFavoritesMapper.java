package ru.shop_example.user_service.mapper;

import org.mapstruct.Mapper;
import ru.shop_example.user_service.dto.ResponseUserFavoritesDto;
import ru.shop_example.user_service.entity.UserFavorites;

@Mapper(componentModel = "spring")
public interface UserFavoritesMapper {

    ResponseUserFavoritesDto userFavoritesToUserFavoritesDto(UserFavorites userFavorites);
}
