package ru.shop_example.product_service.mapper;

import org.mapstruct.Mapper;
import ru.shop_example.product_service.dto.ProductSearchParamsDto;
import ru.shop_example.product_service.entity.ProductSearchParams;

@Mapper(componentModel = "spring")
public interface ProductSearchParamsMapper {

    ProductSearchParams productSearchParamsDtoToProductSearchParams(ProductSearchParamsDto productSearchParamsDto);
}
