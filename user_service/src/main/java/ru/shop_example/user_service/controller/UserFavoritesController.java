package ru.shop_example.user_service.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.shop_example.user_service.dto.ResponseErrorDto;
import ru.shop_example.user_service.dto.ResponseUserFavoritesDto;
import ru.shop_example.user_service.service.UserFavoritesService;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("favorites")
@Slf4j
public class UserFavoritesController {

    private final UserFavoritesService userFavoritesService;


    @Operation(
            summary = "Получение списка избранного пользователя",
            description = "Возвращает DTO с данными о пользователе",
            tags = {"AUTHENTICATED"},
            security = @SecurityRequirement(name = "bearerAuth"),
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Данные получены",
                            content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ResponseUserFavoritesDto.class))}),
            })
    @GetMapping
    public ResponseUserFavoritesDto getUserFavorites(@RequestHeader("user-id") UUID userId){
        return userFavoritesService.get(userId);
    }

    @Operation(
            summary = "Добавление продукта в список избранного",
            description = "Добавляет указанный продукт в список избранного текущего пользователя",
            tags = {"AUTHENTICATED"},
            security = @SecurityRequirement(name = "bearerAuth"),
            responses = {
                    @ApiResponse(
                            responseCode = "204",
                            description = "Данные обновлены"),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Продукт не найден",
                            content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ResponseErrorDto.class))}),
            })
    @PutMapping("{productId}")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public void addUserFavorites(@RequestHeader("user-id") UUID userId, @PathVariable("productId") UUID productId){
        userFavoritesService.add(userId, productId);
    }

    @Operation(
            summary = "Удаление продукта из списка избранного",
            description = "Удаляет указанный продукт из списка избранного текущего пользователя",
            tags = {"AUTHENTICATED"},
            security = @SecurityRequirement(name = "bearerAuth"),
            responses = {
                    @ApiResponse(
                            responseCode = "204",
                            description = "Данные удалены"),
            })
    @DeleteMapping("{productId}")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public void removeUserFavorites(@RequestHeader("user-id") UUID userId, @PathVariable("productId") UUID productId){
        userFavoritesService.remove(userId, productId);
    }

    @Operation(
            summary = "Очищает список избранного",
            description = "Удаляет все продукта из списка избранного текущего пользователя",
            tags = {"AUTHENTICATED"},
            security = @SecurityRequirement(name = "bearerAuth"),
            responses = {
                    @ApiResponse(
                            responseCode = "204",
                            description = "Данные удалены"),
            })
    @DeleteMapping
    public void clearUserFavorites(@RequestHeader("user-id") UUID userId){
        userFavoritesService.clear(userId);
    }
}
