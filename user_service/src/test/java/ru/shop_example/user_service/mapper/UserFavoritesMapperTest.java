package ru.shop_example.user_service.mapper;


import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import ru.shop_example.user_service.dto.ResponseUserFavoritesDto;
import ru.shop_example.user_service.entity.UserFavorites;

import java.time.Instant;
import java.util.Set;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class UserFavoritesMapperTest {

    UserFavoritesMapper mapper = Mappers.getMapper(UserFavoritesMapper.class);

    @Test
    @DisplayName("Должен возвращать корректное дто")
    void userFavoritesToUserFavoritesDtoShouldReturnCorrectDto(){

        //Arrange
        UserFavorites userFavorites = createUserFavorites();
        ResponseUserFavoritesDto expected = new ResponseUserFavoritesDto(Set.copyOf(userFavorites.getValues()));

        //Act
        ResponseUserFavoritesDto actual = mapper.userFavoritesToUserFavoritesDto(userFavorites);

        //Verify
        assertEquals(expected, actual);
    }

    private UserFavorites createUserFavorites(){
        return UserFavorites.builder()
                .userId(UUID.randomUUID())
                .lastChange(Instant.now())
                .values(Set.of(UUID.randomUUID(), UUID.randomUUID(), UUID.randomUUID()))
                .build();
    }
}
