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
import ru.shop_example.user_service.service.UserSessionService;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("session")
@Slf4j
public class UserSessionController {

    private final UserSessionService userSessionService;

    @Operation(
            summary = "Завершение текущей сессии",
            description = "Завершает текущую сессии и обнуляет доступ для связанных токенов",
            tags = {"AUTHENTICATED"},
            security = @SecurityRequirement(name = "bearerAuth"),
            responses = {
                    @ApiResponse(
                            responseCode = "204",
                            description = "Сессия завершена"),
            })
    @DeleteMapping("terminate")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public void terminateCurrentSession(@RequestHeader("session-id") UUID sessionId){
        log.info("Called terminateCurrentSession controller method");
        userSessionService.terminateCurrentSessionBySessionId(sessionId);
    }

    @Operation(
            summary = "Завершение всех сессий пользователя",
            description = "Завершает все сессии и обнуляет доступ для связанных токенов",
            tags = {"AUTHENTICATED"},
            security = @SecurityRequirement(name = "bearerAuth"),
            responses = {
                    @ApiResponse(
                            responseCode = "204",
                            description = "Сессии завершены"),
            })
    @DeleteMapping("terminateAll")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public void terminateAllSessions(@RequestHeader("user-id") UUID userId){
        log.info("Called terminateAllSessions controller method");
        userSessionService.terminateAllSessionsByUserId(userId);
    }

}
