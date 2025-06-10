package ru.shop_example.product_service.dto;

import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.shop_example.product_service.validation.custom.TwoParamsCompare;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@TwoParamsCompare(smallerParam = "minPrice", biggerParam = "maxPrice", allowEqual = true)
public class ProductSearchParamsDto {

    private String name;
    @PositiveOrZero()
    private BigDecimal minPrice;
    @PositiveOrZero()
    private BigDecimal maxPrice;
    @PositiveOrZero()
    private Integer minStock;
    private UUID productTypeId;
}
