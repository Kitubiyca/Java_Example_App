package ru.shop_example.user_service.controller;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.shop_example.user_service.dto.*;
import ru.shop_example.user_service.service.UserService;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("user")
@Slf4j
public class UserController {

    private final UserService userService;

    @Operation(
            summary = "Получение информации о профиле пользователя",
            description = "Возвращает DTO с данными о пользователе",
            tags = {"AUTHENTICATED"},
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Данные получены",
                            content = {@Content(mediaType = "application/json", schema = @Schema(implementation = UserInfoDto.class))}),
                    @ApiResponse(
                            responseCode = "500",
                            description = "Ошибка сервера",
                            content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorDto.class))}),
            })
    @GetMapping
    public UserInfoDto getUserProfile(@RequestHeader("user-id") UUID userId){
        return userService.getProfile(userId);
    }

    @Operation(
            summary = "Получение информации о профиле пользователя",
            description = "Возвращает DTO с данными о пользователе",
            tags = {"AUTHENTICATED", "ROLE_MANAGER"},
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Данные получены",
                            content = {@Content(mediaType = "application/json", schema = @Schema(implementation = UserInfoDto.class))}),
                    @ApiResponse(
                            responseCode = "500",
                            description = "Ошибка сервера",
                            content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorDto.class))}),
            })
    @GetMapping("test")
    public UserInfoDto getUserProfileTest(@RequestHeader("user-id") UUID userId){
        return userService.getProfile(userId); //TODO remove test endpoint
    }

    @Operation(
            summary = "Обновление данных профиля пользователя",
            description = "Обновляет данные профиля пользователя в БД",
            tags = {"AUTHENTICATED"},
            responses = {
                    @ApiResponse(
                            responseCode = "204",
                            description = "Данные обновлены"),
                    @ApiResponse(
                            responseCode = "500",
                            description = "Ошибка сервера",
                            content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorDto.class))}),
            })
    @PatchMapping
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public void updateUserProfile(@RequestHeader("user-id") UUID userId, @RequestBody @Validated UpdateUserProfileDto updateUserProfileDto){
        userService.updateProfile(userId, updateUserProfileDto);
    }

    @Operation(
            summary = "Удаление профиля пользователя",
            description = "Удаляеь данные о профиле пользователя",
            tags = {"AUTHENTICATED"},
            responses = {
                    @ApiResponse(
                            responseCode = "204",
                            description = "Данные удалены"),
                    @ApiResponse(
                            responseCode = "500",
                            description = "Ошибка сервера",
                            content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorDto.class))}),
            })
    @DeleteMapping
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public void deleteUserProfile(@RequestHeader("user-id") UUID userId, @RequestBody @Validated PasswordDto passwordDto){
        userService.deleteProfile(userId, passwordDto);
    }
}
