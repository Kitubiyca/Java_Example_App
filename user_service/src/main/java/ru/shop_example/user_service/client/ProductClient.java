package ru.shop_example.user_service.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import ru.shop_example.user_service.dto.BooleanDto;

import java.util.UUID;

@FeignClient(name = "product-service", path = "product")
public interface ProductClient {

    @GetMapping("/entity/get/{productId}/exists")
    BooleanDto isProductExist(@PathVariable("productId") UUID productId);
}
