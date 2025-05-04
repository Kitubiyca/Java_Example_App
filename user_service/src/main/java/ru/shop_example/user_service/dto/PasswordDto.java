package ru.shop_example.user_service.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter(AccessLevel.PUBLIC)
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class PasswordDto {

    @NotBlank
    public String password;
}
