package ru.shop_example.user_service.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDate;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "DTO с новыми данными пользователя")
public class RequestUpdateUserProfileDto {

    @Email
    @NotBlank
    @Schema(description = "Электронная почта", example = "example@example.com")
    private String email;
    @NotBlank
    @Schema(description = "Пароль", example = "1234")
    private String oldPassword;
    @NotBlank
    @Schema(description = "Пароль", example = "4321")
    private String password;
    @NotBlank
    @Schema(description = "Имя", example = "Иван")
    private String firstname;
    @NotBlank
    @Schema(description = "Фамилия", example = "Иванов")
    private String lastname;
    @NotBlank
    @Schema(description = "Отчество", example = "Иванович")
    private String patronymic;
    @NotBlank
    @Schema(description = "Номер телефона", example = "123456789012")
    private String phoneNumber;
    @NotNull
    @Schema(description = "Дата рождения", example = "1990-01-01")
    private LocalDate birthDate;
}
