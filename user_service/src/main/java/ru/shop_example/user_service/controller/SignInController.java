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
import ru.shop_example.user_service.dto.ErrorDto;
import ru.shop_example.user_service.dto.RefreshTokenDto;
import ru.shop_example.user_service.dto.SignInDto;
import ru.shop_example.user_service.dto.SignInResponseDto;
import ru.shop_example.user_service.service.SignInService;

@RestController
@RequiredArgsConstructor
@RequestMapping("user/sign-in")
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
                            content = {@Content(mediaType = "application/json", schema = @Schema(implementation = SignInResponseDto.class))}),
                    @ApiResponse(
                            responseCode = "500",
                            description = "Ошибка сервера",
                            content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorDto.class))}),
            })
    @PostMapping("/by-password")
    public SignInResponseDto signInWithPassword(@RequestBody @Validated SignInDto signInDto) {
        log.info("Called signInWithPassword controller method");
        return signInService.signIn(signInDto);
    }

    @Operation(
            summary = "Вход в аккаунт через токен",
            description = "Возвращает DTO с данными для доступа к остальной части API",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Успешная авторизация",
                            content = {@Content(mediaType = "application/json", schema = @Schema(implementation = SignInResponseDto.class))}),
                    @ApiResponse(
                            responseCode = "500",
                            description = "Ошибка сервера",
                            content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorDto.class))}),
            })
    @PostMapping("/refresh-by-token")
    public SignInResponseDto signInWithRefreshToken(@RequestBody @Validated RefreshTokenDto RefreshTokenDto) {
        log.info("Called signInWithRefreshToken controller method");
        return signInService.signInWithRefreshToken(RefreshTokenDto);
    }
}
