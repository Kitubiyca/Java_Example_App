package ru.shop_example.user_service.repository;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import ru.shop_example.user_service.common.containers.MongoContainer;
import ru.shop_example.user_service.entity.UserFavorites;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;

@DataMongoTest
@ActiveProfiles("test")
public class UserFavoritesRepositoryIT implements MongoContainer {

    @Autowired
    private UserFavoritesRepository userFavoritesRepository;

    @Test
    @DisplayName("Должен корректно создавать новый документ и добавлять в него элементы")
    void shouldCorrectlyCreateNewDocumentAndAddItem(){

        //Arrange
        UUID userId = UUID.randomUUID();
        Set<UUID> items = Set.of(UUID.randomUUID(), UUID.randomUUID());
        Iterator<UUID> itemsIter = items.iterator();

        //Act
        userFavoritesRepository.add(userId, itemsIter.next());
        userFavoritesRepository.add(userId, itemsIter.next());

        //Verify
        Optional<UserFavorites> result = userFavoritesRepository.findById(userId);
        assertThat(result).isNotEmpty();
        assertThat(result.get().getUserId()).isEqualTo(userId);
        assertThat(result.get().getValues()).isEqualTo(items);
    }

    @Test
    @DisplayName("Должен корректно вернуть несуществующий документ")
    void shouldNotReturnDocumentIfNotPresent(){

        //Arrange
        UUID userId = UUID.randomUUID();

        //Act
        Optional<UserFavorites> result = userFavoritesRepository.findById(userId);

        //Verify
        assertThat(result).isEmpty();
    }

    @Test
    @DisplayName("Должен корректно удалять элементы из документа")
    void shouldCorrectlyRemoveItemFromDocument(){

        //Arrange
        UUID userId = UUID.randomUUID();
        UUID item1 = UUID.randomUUID();
        UUID item2 = UUID.randomUUID();

        //Act
        userFavoritesRepository.add(userId, item1);
        userFavoritesRepository.add(userId, item2);
        userFavoritesRepository.remove(userId, item1);

        //Verify
        Optional<UserFavorites> result = userFavoritesRepository.findById(userId);
        assertThat(result).isNotEmpty();
        assertThat(result.get().getUserId()).isEqualTo(userId);
        assertThat(result.get().getValues()).isEqualTo(Set.of(item2));
    }

    @Test
    @DisplayName("Должен корректно очистить список элементов в документе")
    void shouldCorrectlyClearItemsFromDocument(){

        //Arrange
        UUID userId = UUID.randomUUID();
        UUID item1 = UUID.randomUUID();
        UUID item2 = UUID.randomUUID();

        //Act
        userFavoritesRepository.add(userId, item1);
        userFavoritesRepository.add(userId, item2);
        userFavoritesRepository.clear(userId);

        //Verify
        Optional<UserFavorites> result = userFavoritesRepository.findById(userId);
        assertThat(result).isNotEmpty();
        assertThat(result.get().getUserId()).isEqualTo(userId);
        assertThat(result.get().getValues()).isEqualTo(Set.of());
    }
}
