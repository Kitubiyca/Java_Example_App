package ru.shop_example.user_service.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import ru.shop_example.user_service.entity.constant.UserStatus;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Schema(description = "DTO с данными о пользователе")
public class ResponseUserInfoDto {

    @Schema(description = "Id", example = "323e33e1-18f1-4bdf-b6b7-42db966c2229")
    private UUID id;
    @Schema(description = "Электронная почта", example = "example@example.com")
    private String email;
    @Schema(description = "Имя", example = "Иван")
    private String firstname;
    @Schema(description = "Фамилия", example = "Иванов")
    private String lastname;
    @Schema(description = "Отчество", example = "Иванович")
    private String patronymic;
    @Schema(description = "Номер телефона", example = "123456789012")
    private String phoneNumber;
    @Schema(description = "Дата рождения", example = "1990-01-01")
    private LocalDate birthDate;
    @Schema(description = "Дата входа в аккаунт", example = "2025-06-17T15:31:41.221527506")
    private OffsetDateTime accessDate;
    @Schema(description = "Дата регистрации", example = "2025-06-17T15:31:41.221527506")
    private OffsetDateTime registrationDate;
    @Schema(description = "Статус", example = "active")
    private UserStatus status;
    @Schema(description = "Id роли", example = "323e33e1-18f1-4bdf-b6b7-42db966c2229")
    private UUID roleId;
}
