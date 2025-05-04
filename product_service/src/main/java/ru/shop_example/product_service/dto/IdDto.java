package ru.shop_example.product_service.dto;

import lombok.*;

import java.util.UUID;

@Getter
@Setter(AccessLevel.PUBLIC)
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class IdDto {

    UUID id;
}
