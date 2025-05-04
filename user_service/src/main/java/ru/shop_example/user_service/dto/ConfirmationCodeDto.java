package ru.shop_example.user_service.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.util.UUID;

@Getter
@Setter(AccessLevel.PUBLIC)
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class ConfirmationCodeDto {

    @NotBlank
    private UUID id;
    @NotBlank
    private String value;
}
