package ru.shop_example.gateway.repository;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.shop_example.gateway.entity.ServiceData;

public interface ServiceDataReactiveRepository {

    Mono<Boolean> save(ServiceData serviceData);
    Mono<ServiceData> find(String id);
    Flux<ServiceData> findAll();
}
