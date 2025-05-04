package ru.shop_example.user_service.dto;

import lombok.*;

import java.util.UUID;

@Getter
@Setter(AccessLevel.PUBLIC)
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class UserIdDto {

    private UUID id;
}
