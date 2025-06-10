package ru.shop_example.product_service.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import ru.shop_example.product_service.entity.Product;

import java.util.UUID;

public interface ProductRepository extends JpaRepository<Product, UUID>, JpaSpecificationExecutor<Product> {

    public Page<Product> findAllByProductTypeId(UUID productTypeId, Pageable pageable);

    @EntityGraph(value = "Product.withType")
    public Page<Product> findAllWithTypeByProductTypeId(UUID productTypeId, Pageable pageable);
}
