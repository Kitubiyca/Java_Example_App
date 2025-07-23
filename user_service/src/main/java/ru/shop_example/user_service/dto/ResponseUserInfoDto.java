package ru.shop_example.user_service.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import ru.shop_example.user_service.entity.User;
import ru.shop_example.user_service.entity.constant.UserStatus;
import ru.shop_example.user_service.entity.Role;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.UUID;

/**
 * Исходящее дто с данными о пользователе.
 *
 * @see User
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Schema(description = "DTO с данными о пользователе")
public class ResponseUserInfoDto {

    /** Id. */
    @Schema(description = "Id", example = "323e33e1-18f1-4bdf-b6b7-42db966c2229")
    private UUID id;

    /** Электронная почта. */
    @Schema(description = "Электронная почта", example = "example@example.com")
    private String email;

    /** Имя. */
    @Schema(description = "Имя", example = "Иван")
    private String firstname;

    /** Фамилия. */
    @Schema(description = "Фамилия", example = "Иванов")
    private String lastname;

    /** Отчество. */
    @Schema(description = "Отчество", example = "Иванович")
    private String patronymic;

    /** Номер телефона формата "123456789012". */
    @Schema(description = "Номер телефона", example = "123456789012")
    private String phoneNumber;

    /** Дата рождения формата "1990-01-01". */
    @Schema(description = "Дата рождения", example = "1990-01-01")
    private LocalDate birthDate;

    /** Дата входа в аккаунт формата 2025-06-17T15:31:41.221527506. */
    @Schema(description = "Дата входа в аккаунт", example = "2025-06-17T15:31:41.221527506")
    private OffsetDateTime accessDate;

    /** Дата регистрации формата 2025-06-17T15:31:41.221527506. */
    @Schema(description = "Дата регистрации", example = "2025-06-17T15:31:41.221527506")
    private OffsetDateTime registrationDate;

    /** Статус. */
    @Schema(description = "Статус", example = "active")
    private UserStatus status;

    /**
     * Id роли.
     *
     * @see Role
     * */
    @Schema(description = "Id роли", example = "323e33e1-18f1-4bdf-b6b7-42db966c2229")
    private UUID roleId;
}
