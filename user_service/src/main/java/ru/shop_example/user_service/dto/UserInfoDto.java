package ru.shop_example.user_service.dto;

import lombok.*;
import ru.shop_example.user_service.entity.constant.UserStatus;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.UUID;

@Getter
@Setter(AccessLevel.PUBLIC)
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class UserInfoDto {

    private UUID id;
    private String email;
    private String firstname;
    private String lastname;
    private String patronymic;
    private String phoneNumber;
    private LocalDate birthDate;
    private OffsetDateTime accessDate;
    private OffsetDateTime registrationDate;
    private UserStatus status;
    private UUID roleId;
}
