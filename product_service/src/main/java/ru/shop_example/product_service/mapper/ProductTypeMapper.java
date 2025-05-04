package ru.shop_example.product_service.mapper;

import org.mapstruct.Mapper;
import ru.shop_example.product_service.dto.ProductTypeDto;
import ru.shop_example.product_service.entity.ProductType;

@Mapper(componentModel = "spring")
public interface ProductTypeMapper {

    ProductType productTypeDtoToProductType(ProductTypeDto productDto);

    ProductTypeDto productTypeToProductTypeDto(ProductType product);
}
