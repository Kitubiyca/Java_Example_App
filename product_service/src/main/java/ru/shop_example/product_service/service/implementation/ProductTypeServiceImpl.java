package ru.shop_example.product_service.service.implementation;

import jakarta.ws.rs.NotFoundException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
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
        return productTypeRepository.save(productType);
    }

    public ProductType updateProductType(ProductType productType){
        log.info("Called updateProductType service method");
        if (productTypeRepository.existsById(productType.getId())) throw new NotFoundException(String.format("Product with id %s not found", productType.getId()));
        return productTypeRepository.save(productType);
    }

    public void deleteProductTypeById(UUID id){
        log.info("Called deleteProductTypeById service method");
        productTypeRepository.deleteById(id);
    }
}
