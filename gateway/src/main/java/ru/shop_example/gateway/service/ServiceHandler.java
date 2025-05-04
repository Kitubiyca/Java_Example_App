package ru.shop_example.gateway.service;

import io.swagger.v3.oas.models.OpenAPI;
import reactor.core.publisher.Flux;

public interface ServiceHandler {
    void handleServices (Flux<String> services);
}
