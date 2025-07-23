package ru.shop_example.user_service.service;

import ru.shop_example.user_service.dto.RequestRefreshTokenDto;
import ru.shop_example.user_service.dto.RequestSignInDto;
import ru.shop_example.user_service.dto.ResponseSignInDto;
import ru.shop_example.user_service.controller.SignInController;
import ru.shop_example.user_service.exception.custom.AuthorizationFailedException;
import ru.shop_example.user_service.exception.custom.RequestDeniedException;
import ru.shop_example.user_service.exception.custom.UserNotFoundException;

/**
 * Сервис для обработки авторизации пользователя в приложении.
 *
 * @see SignInController
 */
public interface SignInService {

    /**
     * Авторизация пользователя по электронной почте и паролю.
     *
     * @param requestSignInDto электронная почта и пароль в дто
     *
     * @return связанные access и refresh токены
     *
     * @throws UserNotFoundException если пользователь с такой электронной почтой не найден
     * @throws RequestDeniedException неверный статус пользователя
     * @throws AuthorizationFailedException неверный пароль
     */
    ResponseSignInDto signIn(RequestSignInDto requestSignInDto);

    /**
     * Авторизация пользователя по refresh токену.
     *
     * @param requestRefreshTokenDto refresh токен в дто
     *
     * @return новые связанные access и refresh токены
     *
     * @throws UserNotFoundException если пользователь с id из токена не найден
     * @throws RequestDeniedException неверный статус пользователя
     * @throws AuthorizationFailedException неверный refresh токен
     */
    ResponseSignInDto signInWithRefreshToken(RequestRefreshTokenDto requestRefreshTokenDto);
}
