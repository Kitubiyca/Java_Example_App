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
import ru.shop_example.user_service.service.SignUpService;

/**
 * Контроллер, отвечающий за регистрацию новых пользователей.
 *
 * @see SignUpService
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("sign-up")
@Slf4j
public class SignUpController {

    private final SignUpService signUpService;

    /**
     * Запрос на регистрацию нового пользователя.
     *
     * @param requestSignUpDto дто с данными пользователя
     *
     * @return дто с id для связки с кодом подтверждения
     */
    @Operation(
            summary = "Регистрация нового аккаунта",
            description = "Начинает процесс регистрации нового пользователя",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Успешное начало регистрации",
                            content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ResponseOTPIdDto.class))}),
            })
    @PostMapping("/request")
    public ResponseOTPIdDto requestSignUp(@RequestBody @Validated RequestSignUpDto requestSignUpDto) {
        log.info("Called requestSignUp controller method");
        return signUpService.registerUser(requestSignUpDto);
    }

    /**
     * Запрос на повторение кода подтверждения.
     *
     * @param requestEmailDto дто с электронной почтой
     *
     * @return дто с id для связки с кодом подтверждения
     */
    @Operation(
            summary = "Повторение кода подтверждения",
            description = "Повторяет отправку кода подтверждения для регистрации",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Код успешно отправлен",
                            content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ResponseOTPIdDto.class))}),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Пользователь не найден",
                            content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ResponseErrorDto.class))}),
            })
    @PostMapping("/resend-confirmation-code")
    public ResponseOTPIdDto resendOTPWithEmail(@RequestBody @Validated RequestEmailDto requestEmailDto) {
        log.info("Called resendOTPWithEmail controller method");
        return signUpService.resendOTPWithEmail(requestEmailDto);
    }

    /**
     * Завершение регистрации с помощью кода подтверждения.
     *
     * @param RequestOTPDto дто со связкой id + код подтверждения
     */
    @Operation(
            summary = "Подтверждение регистрации",
            description = "Завершает процесс регистрации",
            responses = {
                    @ApiResponse(
                            responseCode = "201",
                            description = "Регистрация успешно завершена"),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Пользователь не найден",
                            content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ResponseErrorDto.class))}),
            })
    @PostMapping("/confirm")
    @ResponseStatus(code = HttpStatus.CREATED)
    public void confirmSignUp(@RequestBody @Validated RequestOTPDto RequestOTPDto) {
        log.info("Called confirmSignUp controller method");
        signUpService.confirmRegistration(RequestOTPDto);
    }
}
