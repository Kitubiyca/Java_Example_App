package ru.shop_example.user_service.service.implementation;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.shop_example.user_service.client.ProductClient;
import ru.shop_example.user_service.controller.UserFavoritesController;
import ru.shop_example.user_service.dto.ResponseUserFavoritesDto;
import ru.shop_example.user_service.exception.custom.EntityNotFoundException;
import ru.shop_example.user_service.mapper.UserFavoritesMapper;
import ru.shop_example.user_service.repository.UserFavoritesRepository;
import ru.shop_example.user_service.service.UserFavoritesService;
import ru.shop_example.user_service.entity.UserFavorites;

import java.util.UUID;
/**
 * Реализация сервиса для работы со списком избранного пользователя.
 *
 * @see UserFavorites
 * @see UserFavoritesController
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class UserFavoritesServiceImpl implements UserFavoritesService {

    private final UserFavoritesRepository userFavoritesRepository;
    private final UserFavoritesMapper userFavoritesMapper;
    private final ProductClient productClient;

    /**
     * {@inheritDoc}
     */
    @Override
    public ResponseUserFavoritesDto get(UUID userId) {
        log.info("Called UserFavoritesService.get service method");
        return userFavoritesMapper.userFavoritesToUserFavoritesDto(userFavoritesRepository.findById(userId).orElseThrow(() -> new EntityNotFoundException("Data for user " + userId + " not found.")));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void add(UUID userId, UUID productId) {
        log.info("Called UserFavoritesService.add service method");
        if (!productClient.isProductExist(productId).getValue()) throw new EntityNotFoundException("Product " + productId + " not found.");
        userFavoritesRepository.add(userId, productId);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void remove(UUID userId, UUID productId) {
        log.info("Called UserFavoritesService.remove service method");
        userFavoritesRepository.remove(userId, productId);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void clear(UUID userId) {
        log.info("Called UserFavoritesService.clear service method");
        userFavoritesRepository.clear(userId);
    }
}
