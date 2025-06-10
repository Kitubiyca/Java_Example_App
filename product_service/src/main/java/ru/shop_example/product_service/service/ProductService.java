package ru.shop_example.product_service.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ru.shop_example.product_service.dto.BooleanDto;
import ru.shop_example.product_service.entity.Product;
import ru.shop_example.product_service.entity.ProductSearchParams;

import java.util.UUID;

public interface ProductService {
    public Product getProductById(UUID id);
    public Product createProduct(Product product);
    public Product updateProduct(Product product);
    public void deleteProductById(UUID id);
    public Page<Product> getAllProducts(Pageable pageable);
    public Page<Product> getAllProductsByTypeId(UUID typeId, Pageable pageable);
    public Page<Product> searchProducts(ProductSearchParams productSearchParams, Pageable pageable);
    public BooleanDto isProductExist(UUID id);
}
