package ru.shop_example.product_service.repository;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.shop_example.product_service.entity.Product;

import java.util.List;
import java.util.UUID;

public interface ProductRepository extends JpaRepository<Product, UUID> {

    public List<Product> findAllByProductTypeId(UUID productTypeId);

    @EntityGraph(value = "Product.withType")
    public List<Product> findAllWithTypeByProductTypeId(UUID productTypeId);
}
