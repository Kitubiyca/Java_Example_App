package ru.shop_example.notification_service.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter(AccessLevel.PUBLIC)
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class OTPDto {

    @NotBlank
    private String name;
    @Email
    @NotBlank
    private String email;
    @NotBlank
    private String code;
}
