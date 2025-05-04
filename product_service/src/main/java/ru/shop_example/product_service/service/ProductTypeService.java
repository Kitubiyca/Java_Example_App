package ru.shop_example.product_service.service;

import ru.shop_example.product_service.entity.ProductType;

import java.util.UUID;

public interface ProductTypeService {
    public ProductType getProductTypeById(UUID id);
    public ProductType createProductType(ProductType productType);
    public ProductType updateProductType(ProductType productType);
    public void deleteProductTypeById(UUID id);
}
