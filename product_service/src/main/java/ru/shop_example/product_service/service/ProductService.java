package ru.shop_example.product_service.service;

import ru.shop_example.product_service.entity.Product;

import java.util.List;
import java.util.UUID;

public interface ProductService {
    public Product getProductById(UUID id);
    public Product createProduct(Product product);
    public Product updateProduct(Product product);
    public void deleteProductById(UUID id);
    public List<Product> getAllProducts();
    public List<Product> getAllProductsByTypeId(UUID typeId);
}
