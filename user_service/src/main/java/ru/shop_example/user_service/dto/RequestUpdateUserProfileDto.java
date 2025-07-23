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
    @Email
    @NotBlank
    @Schema(description = "Электронная почта", example = "example@example.com")
    private String email;

    /** Старый пароль (для подтверждения). */
    @NotBlank
    @Schema(description = "Пароль", example = "1234")
    private String oldPassword;

    /** Новый пароль (может совпадать со старым). */
    @NotBlank
    @Schema(description = "Пароль", example = "4321")
    private String password;

    /** Имя. */
    @NotBlank
    @Schema(description = "Имя", example = "Иван")
    private String firstname;

    /** Фамилия. */
    @NotBlank
    @Schema(description = "Фамилия", example = "Иванов")
    private String lastname;

    /** Отчество. */
    @NotBlank
    @Schema(description = "Отчество", example = "Иванович")
    private String patronymic;

    /** номер телефона. */
    @NotBlank
    @Schema(description = "Номер телефона", example = "123456789012")
    private String phoneNumber;

    /** Дата рождения формата 1990-01-01. */
    @NotNull
    @Schema(description = "Дата рождения", example = "1990-01-01")
    private LocalDate birthDate;
}
