package ru.shop_example.product_service.service.implementation;

import jakarta.ws.rs.NotFoundException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.shop_example.product_service.entity.Product;
import ru.shop_example.product_service.repository.ProductRepository;
import ru.shop_example.product_service.service.ProductService;

import java.util.List;
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
        return productRepository.save(product);
    }

    public Product updateProduct(Product product){
        log.info("Called updateProduct service method");
        if (!productRepository.existsById(product.getId())) throw new NotFoundException(String.format("Product with id %s not found", product.getId()));
        return productRepository.save(product);
    }

    public void deleteProductById(UUID id){
        log.info("Called deleteProductById service method");
        productRepository.deleteById(id);
    }

    public List<Product> getAllProducts(){
        log.info("Called getAllProducts service method");
        return productRepository.findAll();
    }

    public List<Product> getAllProductsByTypeId(UUID typeId){
        log.info("Called getAllProductsByTypeId service method");
        return productRepository.findAllByProductTypeId(typeId);
    }
}
