package ru.shop_example.gateway.service;

import io.swagger.v3.oas.models.OpenAPI;
import reactor.core.publisher.Mono;

public interface OpenApiDocRetriever {

    Mono<OpenAPI> getServiceApiDocByName(String serviceName);

}
