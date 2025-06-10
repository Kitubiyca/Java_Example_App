package ru.shop_example.product_service.repository.specification;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import org.springframework.data.jpa.domain.Specification;
import ru.shop_example.product_service.entity.Product;
import ru.shop_example.product_service.entity.ProductSearchParams;

import java.math.BigDecimal;
import java.util.UUID;

public class ProductSpecification {

    public static Specification<Product> nameContains(String name) {
        return (Root<Product> root, CriteriaQuery<?> query, CriteriaBuilder cb) -> {
            if (name == null || name.trim().isEmpty()) {
                return cb.conjunction();
            }
            return cb.like(
                    cb.lower(root.get("name")),
                    "%" + name.trim().toLowerCase() + "%"
            );
        };
    }

    public static Specification<Product> priceGreaterThanOrEqual(BigDecimal minPrice) {
        return (Root<Product> root, CriteriaQuery<?> query, CriteriaBuilder cb) -> {
            if (minPrice == null) {
                return cb.conjunction();
            }
            return cb.greaterThanOrEqualTo(root.get("price"), minPrice);
        };
    }

    public static Specification<Product> priceLessThanOrEqual(BigDecimal maxPrice) {
        return (Root<Product> root, CriteriaQuery<?> query, CriteriaBuilder cb) -> {
            if (maxPrice == null) {
                return cb.conjunction();
            }
            return cb.lessThanOrEqualTo(root.get("price"), maxPrice);
        };
    }

    public static Specification<Product> stockGreaterThanOrEqual(Integer minStock) {
        return (Root<Product> root, CriteriaQuery<?> query, CriteriaBuilder cb) -> {
            if (minStock == null) {
                return cb.conjunction();
            }
            return cb.greaterThanOrEqualTo(root.get("stock"), minStock);
        };
    }

    public static Specification<Product> hasProductTypeId(UUID productTypeId) {
        return (Root<Product> root, CriteriaQuery<?> query, CriteriaBuilder cb) -> {
            if (productTypeId == null) {
                return cb.conjunction();
            }
            return cb.equal(root.get("productTypeId"), productTypeId);
        };
    }

    public static Specification<Product> searchByParams(ProductSearchParams productSearchParams){
        return nameContains(productSearchParams.getName())
                .and(priceGreaterThanOrEqual(productSearchParams.getMinPrice()))
                .and(priceLessThanOrEqual(productSearchParams.getMaxPrice()))
                .and(stockGreaterThanOrEqual(productSearchParams.getMinStock()))
                .and(hasProductTypeId(productSearchParams.getProductTypeId()));
    }

}
