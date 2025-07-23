package ru.shop_example.user_service.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDate;

/**
 * Входящее дто с данными для обновления профиля пользователя.
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "DTO с новыми данными пользователя")
public class RequestUpdateUserProfileDto {

    /** Электронная почта. */
    @Email(message = "email must follow email pattern")
    @NotBlank(message = "email must be not null or blank")
    @Schema(description = "Электронная почта", example = "example@example.com")
    private String email;

    /** Старый пароль (для подтверждения). */
    @NotBlank(message = "oldPassword must be not null or blank")
    @Schema(description = "Пароль", example = "1234")
    private String oldPassword;

    /** Новый пароль (может совпадать со старым). */
    @NotBlank(message = "password must be not null or blank")
    @Schema(description = "Пароль", example = "4321")
    private String password;

    /** Имя. */
    @NotBlank(message = "firstname must be not null or blank")
    @Schema(description = "Имя", example = "Иван")
    private String firstname;

    /** Фамилия. */
    @NotBlank(message = "lastname must be not null or blank")
    @Schema(description = "Фамилия", example = "Иванов")
    private String lastname;

    /** Отчество. */
    @NotBlank(message = "patronymic must be not null or blank")
    @Schema(description = "Отчество", example = "Иванович")
    private String patronymic;

    /** номер телефона. */
    @NotBlank(message = "phone number must be not null or blank")
    @Schema(description = "Номер телефона", example = "123456789012")
    private String phoneNumber;

    /** Дата рождения формата 1990-01-01. */
    @NotNull(message = "birthdate must be not null")
    @Schema(description = "Дата рождения", example = "1990-01-01")
    private LocalDate birthDate;
}
