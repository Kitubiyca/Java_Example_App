package ru.shop_example.user_service.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import ru.shop_example.user_service.dto.IntegrationBooleanDto;

import java.util.UUID;

/**
 * Клиент для обращения к эндпоинтам product-service.
 */
@FeignClient(name = "product-service", path = "products")
public interface ProductClient {

    /**
     * Узнать о существование продукта с заданным id.
     *
     * @param productId id продукта
     *
     * @return дто, содержащее булево значение с ответом
     */
    @GetMapping("{productId}/exists")
    IntegrationBooleanDto isProductExist(@PathVariable("productId") UUID productId);
}
