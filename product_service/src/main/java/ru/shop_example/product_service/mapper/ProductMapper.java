package ru.shop_example.product_service.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.shop_example.product_service.dto.ProductDto;
import ru.shop_example.product_service.entity.Product;

import java.util.List;

@Mapper(componentModel = "spring", uses = {ProductTypeMapper.class})
public interface ProductMapper {

    @Mapping(source = "productTypeId", target = "productType.id")
    Product productDtoToProduct(ProductDto productDto);

    @Mapping(source = "productType.id", target = "productTypeId")
    ProductDto productToProductDto(Product product);

    @Mapping(source = "productType.id", target = "productTypeId")
    List<ProductDto> productToProductDto(List<Product> product);
}
