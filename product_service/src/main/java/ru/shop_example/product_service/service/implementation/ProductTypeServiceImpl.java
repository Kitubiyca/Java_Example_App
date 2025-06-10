package ru.shop_example.product_service.service.implementation;

import jakarta.ws.rs.NotFoundException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.shop_example.product_service.entity.Product;
import ru.shop_example.product_service.entity.ProductType;
import ru.shop_example.product_service.repository.ProductTypeRepository;
import ru.shop_example.product_service.service.ProductTypeService;

import java.util.UUID;

@Service
@AllArgsConstructor
@Slf4j
public class ProductTypeServiceImpl implements ProductTypeService {

    private final ProductTypeRepository productTypeRepository;

    public ProductType getProductTypeById(UUID id){
        log.info("Called getProductTypeById service method");
        return productTypeRepository.findById(id).orElseThrow(() -> new NotFoundException(String.format("Product with id %s not found", id)));
    }

    public ProductType createProductType(ProductType productType){
        log.info("Called createProductType service method");
        if (productType.getId() != null) throw new RuntimeException("xxx");//TODO исправить
        return productTypeRepository.save(productType);
    }

    @Transactional
    public ProductType updateProductType(ProductType productType){
        log.info("Called updateProductType service method");
        ProductType savedProductType = productTypeRepository.findById(productType.getId()).orElseThrow(() -> new NotFoundException(String.format("ProductType with id %s not found", productType.getId())));
        savedProductType.setName(productType.getName());
        savedProductType.setDescription(productType.getDescription());
        return productTypeRepository.save(productType);
    }

    public void deleteProductTypeById(UUID id){
        log.info("Called deleteProductTypeById service method");
        productTypeRepository.deleteById(id);
    }
}
