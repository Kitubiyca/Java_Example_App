package ru.shop_example.product_service.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.shop_example.product_service.entity.ProductType;

import java.util.UUID;

public interface ProductTypeRepository extends JpaRepository<ProductType, UUID> {
}
