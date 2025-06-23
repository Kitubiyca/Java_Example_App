package ru.shop_example.user_service.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.shop_example.user_service.dto.ResponseErrorDto;
import ru.shop_example.user_service.dto.RequestRefreshTokenDto;
import ru.shop_example.user_service.dto.RequestSignInDto;
import ru.shop_example.user_service.dto.ResponseSignInDto;
import ru.shop_example.user_service.service.SignInService;

@RestController
@RequiredArgsConstructor
@RequestMapping("sign-in")
@Slf4j
public class SignInController {

    private final SignInService signInService;

    @Operation(
            summary = "Вход в аккаунт с паролем",
            description = "Возвращает DTO с данными для доступа к остальной части API",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Успешная авторизация",
                            content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ResponseSignInDto.class))}),
                    @ApiResponse(
                            responseCode = "401",
                            description = "Ошибка авторизации",
                            content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ResponseErrorDto.class))}),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Пользователь не найден",
                            content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ResponseErrorDto.class))}),
            })
    @PostMapping("/by-password")
    public ResponseSignInDto signInWithPassword(@RequestBody @Validated RequestSignInDto requestSignInDto) {
        log.info("Called signInWithPassword controller method");
        return signInService.signIn(requestSignInDto);
    }

    @Operation(
            summary = "Вход в аккаунт через токен",
            description = "Возвращает DTO с данными для доступа к остальной части API",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Успешная авторизация",
                            content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ResponseSignInDto.class))}),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Пользователь не найден",
                            content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ResponseErrorDto.class))}),
            })
    @PostMapping("/refresh-by-token")
    public ResponseSignInDto signInWithRefreshToken(@RequestBody @Validated RequestRefreshTokenDto RequestRefreshTokenDto) {
        log.info("Called signInWithRefreshToken controller method");
        return signInService.signInWithRefreshToken(RequestRefreshTokenDto);
    }
}
