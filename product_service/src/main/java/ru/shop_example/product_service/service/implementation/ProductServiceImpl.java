package ru.shop_example.product_service.service.implementation;

import jakarta.ws.rs.NotFoundException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.shop_example.product_service.dto.BooleanDto;
import ru.shop_example.product_service.entity.Product;
import ru.shop_example.product_service.entity.ProductSearchParams;
import ru.shop_example.product_service.repository.ProductRepository;
import ru.shop_example.product_service.repository.specification.ProductSpecification;
import ru.shop_example.product_service.service.ProductService;

import java.util.UUID;

@Service
@AllArgsConstructor
@Slf4j
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;

    public Product getProductById(UUID id){
        log.info("Called getProductById service method");
        return productRepository.findById(id).orElseThrow(() -> new NotFoundException(String.format("Product with id %s not found", id)));
    }

    public Product createProduct(Product product){
        log.info("Called createProduct service method");
        if (product.getId() != null) throw new RuntimeException("xxx");//TODO исправить
        return productRepository.save(product);
    }

    @Transactional
    public Product updateProduct(Product product){
        log.info("Called updateProduct service method");
        Product savedProduct = productRepository.findById(product.getId()).orElseThrow(() -> new NotFoundException(String.format("Product with id %s not found", product.getId())));
        savedProduct.setName(product.getName());
        savedProduct.setDescription(product.getDescription());
        savedProduct.setPrice(product.getPrice());
        savedProduct.setStock(product.getStock());
        savedProduct.setProductType(product.getProductType());
        return productRepository.save(product);
    }

    public void deleteProductById(UUID id){
        log.info("Called deleteProductById service method");
        productRepository.deleteById(id);
    }

    public Page<Product> getAllProducts(Pageable pageable){
        log.info("Called getAllProducts service method");
        return productRepository.findAll(pageable);
    }

    public Page<Product> getAllProductsByTypeId(UUID typeId, Pageable pageable){
        log.info("Called getAllProductsByTypeId service method");
        return productRepository.findAllByProductTypeId(typeId, pageable);
    }

    @Override
    public Page<Product> searchProducts(ProductSearchParams productSearchParams, Pageable pageable) {
        log.info("Called searchProducts service method");
        return productRepository.findAll(ProductSpecification.searchByParams(productSearchParams), pageable);
    }

    @Override
    public BooleanDto isProductExist(UUID id) {
        log.info("Called isProductExist service method");
        return new BooleanDto(productRepository.existsById(id));
    }
}
