package ru.shop_example.product_service.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Null;
import lombok.*;

import java.util.UUID;

@Getter
@Setter(AccessLevel.PUBLIC)
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class ProductTypeDto {

    @Null
    private UUID id;
    @NotBlank
    private String name;
    @NotBlank
    private String description;
}
