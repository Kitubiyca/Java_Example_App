package ru.shop_example.gateway.service.implementation;

import io.swagger.v3.oas.models.OpenAPI;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import ru.shop_example.gateway.service.OpenApiDocRetriever;

@RequiredArgsConstructor
@Slf4j
@Service
public class OpenApiDocRetrieverImpl implements OpenApiDocRetriever {

    private final WebClient.Builder webClientBuilder;

    @Override
    public Mono<OpenAPI> getServiceApiDocByName(String serviceName) {
        log.info("Retrieving {} doc", serviceName);
        String uri = String.format("lb://%s/v3/api-docs", serviceName);
        return webClientBuilder.build().get().uri(uri).retrieve().bodyToMono(OpenAPI.class);
    }
}
