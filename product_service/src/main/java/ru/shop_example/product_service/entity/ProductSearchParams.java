package ru.shop_example.product_service.entity;

import lombok.*;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductSearchParams {

    private String name;
    private BigDecimal minPrice;
    private BigDecimal maxPrice;
    private Integer minStock;
    private UUID productTypeId;
}
