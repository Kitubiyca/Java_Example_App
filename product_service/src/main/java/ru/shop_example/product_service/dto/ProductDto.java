package ru.shop_example.product_service.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Null;
import jakarta.validation.constraints.Positive;
import lombok.*;
import ru.shop_example.product_service.validation.groups.OnCreate;
import ru.shop_example.product_service.validation.groups.OnUpdate;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Setter(AccessLevel.PUBLIC)
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class ProductDto {

    @Null(groups = {OnCreate.class})
    @NotNull(groups = {OnUpdate.class})
    private UUID id;
    @NotBlank
    private String name;
    private String description;
    @Positive
    private BigDecimal price;
    @Positive
    private Integer stock;
    @NotNull
    private UUID productTypeId;
}
