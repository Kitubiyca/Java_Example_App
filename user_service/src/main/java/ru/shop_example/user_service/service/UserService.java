package ru.shop_example.user_service.service;

import ru.shop_example.user_service.dto.RequestPasswordDto;
import ru.shop_example.user_service.dto.RequestUpdateUserProfileDto;
import ru.shop_example.user_service.dto.ResponseUserInfoDto;
import ru.shop_example.user_service.entity.User;
import ru.shop_example.user_service.controller.UserController;
import ru.shop_example.user_service.exception.custom.AuthorizationFailedException;
import ru.shop_example.user_service.exception.custom.RequestDeniedException;
import ru.shop_example.user_service.exception.custom.UserNotFoundException;

import java.util.UUID;

/**
 * Сервис для работы с доменной сущностью пользователя.
 *
 * @see User
 * @see UserController
 */
public interface UserService {

    /**
     * Получение информации о пользователе.
     *
     * @param userId id пользователя
     *
     * @throws UserNotFoundException пользователь с указанным id не найден
     *
     * @return дто с информацией о пользователе
     */
    ResponseUserInfoDto getProfile(UUID userId);

    /**
     * Обновление информации о пользователе.
     *
     * @param userID id пользователя
     * @param requestUpdateUserProfileDto дто с новой информацией о пользователе
     *
     * @throws UserNotFoundException пользователь с указанным id не найден
     * @throws RequestDeniedException неверный статус пользователя
     * @throws AuthorizationFailedException неверный пароль
     */
    void updateProfile(UUID userID, RequestUpdateUserProfileDto requestUpdateUserProfileDto);

    /**
     * Удаление профиля пользователя.
     *
     * @param userID id пользователя
     * @param requestPasswordDto дто с паролем пользователя
     *
     * @throws UserNotFoundException пользователь с указанным id не найден
     * @throws RequestDeniedException неверный статус пользователя
     * @throws AuthorizationFailedException неверный пароль
     */
    void deleteProfile(UUID userID, RequestPasswordDto requestPasswordDto);
}
