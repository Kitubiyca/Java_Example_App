package ru.shop_example.user_service.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import ru.shop_example.user_service.dto.IntegrationBooleanDto;

import java.util.UUID;

@FeignClient(name = "product-service", path = "products")
public interface ProductClient {

    @GetMapping("{productId}/exists")
    IntegrationBooleanDto isProductExist(@PathVariable("productId") UUID productId);
}
