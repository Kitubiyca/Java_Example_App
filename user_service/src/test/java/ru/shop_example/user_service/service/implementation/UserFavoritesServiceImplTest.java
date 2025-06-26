package ru.shop_example.user_service.service.implementation;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.shop_example.user_service.client.ProductClient;
import ru.shop_example.user_service.dto.IntegrationBooleanDto;
import ru.shop_example.user_service.dto.ResponseUserFavoritesDto;
import ru.shop_example.user_service.entity.UserFavorites;
import ru.shop_example.user_service.exception.custom.EntityNotFoundException;
import ru.shop_example.user_service.mapper.UserFavoritesMapper;
import ru.shop_example.user_service.repository.UserFavoritesRepository;

import java.time.Instant;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserFavoritesServiceImplTest {

    @Mock
    private UserFavoritesRepository userFavoritesRepository;
    @Mock
    private UserFavoritesMapper userFavoritesMapper;
    @Mock
    private ProductClient productClient;
    @Spy
    @InjectMocks
    private UserFavoritesServiceImpl userFavoritesService;

    @Test
    @DisplayName("Должен возвращать дто с информацией")
    void getShouldReturnDto(){

        //Arrange
        UUID userId = UUID.randomUUID();
        UserFavorites userFavorites = createUserFavorites(userId);
        ResponseUserFavoritesDto expected = new ResponseUserFavoritesDto(userFavorites.getValues());

        when(userFavoritesRepository.findById(userId)).thenReturn(Optional.of(userFavorites));
        when(userFavoritesMapper.userFavoritesToUserFavoritesDto(userFavorites)).thenReturn(expected);

        //Act
        ResponseUserFavoritesDto actual = userFavoritesService.get(userId);

        //Verify
        assertEquals(expected, actual);
    }

    @Test
    @DisplayName("Должен выбрасывать ошибку если пользователь не найден")
    void getShouldThrowEntityNotFoundException(){

        //Arrange
        UUID userId = UUID.randomUUID();

        when(userFavoritesRepository.findById(userId)).thenReturn(Optional.empty());

        //Act
        EntityNotFoundException actual = assertThrows(
                EntityNotFoundException.class,
                () -> userFavoritesService.get(userId));

        //Verify
        assertEquals("Data for user " + userId + " not found.", actual.getMessage());
    }

    @Test
    @DisplayName("Должен успешно сохранять id")
    void addShouldInvokeRepositoryMethod(){

        //Arrange
        UUID userId = UUID.randomUUID();
        UUID productId = UUID.randomUUID();

        when(productClient.isProductExist(productId)).thenReturn(new IntegrationBooleanDto(true));

        //Act
        userFavoritesService.add(userId, productId);

        //Verify
        verify(userFavoritesRepository).add(eq(userId), eq(productId));
    }

    @Test
    @DisplayName("Должен выбрасывать ошибку если продукт не найден")
    void addShouldThrowEntityNotFoundException(){

        //Arrange
        UUID userId = UUID.randomUUID();
        UUID productId = UUID.randomUUID();

        when(productClient.isProductExist(productId)).thenReturn(new IntegrationBooleanDto(false));

        //Act
        EntityNotFoundException actual = assertThrows(
                EntityNotFoundException.class,
                () -> userFavoritesService.add(userId, productId));

        //Verify
        assertEquals("Product " + productId + " not found.", actual.getMessage());
    }

    @Test
    @DisplayName("Должен успешно удалять продукт из списка")
    void removeShouldInvokeRepositoryMethod(){

        //Arrange
        UUID userId = UUID.randomUUID();
        UUID productId = UUID.randomUUID();

        //Act
        userFavoritesService.remove(userId, productId);

        //Verify
        verify(userFavoritesRepository).remove(eq(userId), eq(productId));
    }

    @Test
    @DisplayName("Должен успешно очищать список")
    void clearShouldInvokeRepositoryMethod(){

        //Arrange
        UUID userId = UUID.randomUUID();

        //Act
        userFavoritesService.clear(userId);

        //Verify
        verify(userFavoritesService).clear(eq(userId));
    }

    private UserFavorites createUserFavorites(UUID userId){
        return UserFavorites.builder()
                .userId(userId)
                .lastChange(Instant.now())
                .values(Set.of(UUID.randomUUID(), UUID.randomUUID(), UUID.randomUUID()))
                .build();
    }
}
