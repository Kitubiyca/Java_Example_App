package ru.shop_example.user_service.controller;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.shop_example.user_service.dto.*;
import ru.shop_example.user_service.service.UserService;
import ru.shop_example.user_service.entity.User;

import java.util.UUID;

/**
 * Контроллер, отвечающий за взаимодействие с сущностью {@link User User}.
 *
 * @see UserService
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("user")
@Slf4j
public class UserController {

    private final UserService userService;

    /**
     * Получение информации о пользователе.
     *
     * @param userId UUID пользователя в хэдере (подставляется гейтвеем)
     *
     * @return дто с информацией о пользователе
     */
    @Operation(
            summary = "Получение информации о профиле пользователя",
            description = "Возвращает DTO с данными о пользователе",
            tags = {"AUTHENTICATED"},
            security = @SecurityRequirement(name = "bearerAuth"),
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Данные получены",
                            content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ResponseUserInfoDto.class))}),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Пользователь не найден",
                            content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ResponseErrorDto.class))}),
            })
    @GetMapping
    public ResponseUserInfoDto getUserProfile(@RequestHeader("user-id") UUID userId){
        return userService.getProfile(userId);
    }

    /**
     * Обновление информации о пользователе.
     *
     * @param userId UUID пользователя в хэдере (подставляется гейтвеем)
     * @param requestUpdateUserProfileDto дто с новой информацией о пользователе
     */
    @Operation(
            summary = "Обновление данных профиля пользователя",
            description = "Обновляет данные профиля пользователя в БД",
            tags = {"AUTHENTICATED"},
            security = @SecurityRequirement(name = "bearerAuth"),
            responses = {
                    @ApiResponse(
                            responseCode = "204",
                            description = "Данные обновлены"),
                    @ApiResponse(
                            responseCode = "401",
                            description = "Ошибка авторизации",
                            content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ResponseErrorDto.class))}),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Пользователь не найден",
                            content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ResponseErrorDto.class))}),
            })
    @PatchMapping
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public void updateUserProfile(@RequestHeader("user-id") UUID userId, @RequestBody @Validated RequestUpdateUserProfileDto requestUpdateUserProfileDto){
        userService.updateProfile(userId, requestUpdateUserProfileDto);
    }

    /**
     * Удаление профиля пользователя.
     *
     * @param userId UUID пользователя в хэдере (подставляется гейтвеем)
     * @param requestPasswordDto дто с паролем пользователя
     */
    @Operation(
            summary = "Удаление профиля пользователя",
            description = "Удаляеь данные о профиле пользователя",
            tags = {"AUTHENTICATED"},
            security = @SecurityRequirement(name = "bearerAuth"),
            responses = {
                    @ApiResponse(
                            responseCode = "204",
                            description = "Данные удалены"),
                    @ApiResponse(
                            responseCode = "401",
                            description = "Ошибка авторизации",
                            content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ResponseErrorDto.class))}),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Пользователь не найден",
                            content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ResponseErrorDto.class))}),
            })
    @DeleteMapping
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public void deleteUserProfile(@RequestHeader("user-id") UUID userId, @RequestBody @Validated RequestPasswordDto requestPasswordDto){
        userService.deleteProfile(userId, requestPasswordDto);
    }
}
