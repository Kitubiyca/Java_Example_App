package ru.shop_example.gateway.repository.implementation;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.shop_example.gateway.entity.ServiceData;
import ru.shop_example.gateway.repository.ServiceDataReactiveRepository;

@Repository
@RequiredArgsConstructor
public class ServiceDataReactiveReactiveRepositoryImpl implements ServiceDataReactiveRepository {

    private final ReactiveRedisTemplate<String, ServiceData> reactiveRedisTemplate;

    private final String ID_FORMAT ="serviceData:%s";

    public Mono<Boolean> save(ServiceData serviceData){
        return reactiveRedisTemplate.opsForValue().set(
                String.format(ID_FORMAT, serviceData.getName()),
                serviceData);
    }

    public Mono<ServiceData> find(String id){
        return reactiveRedisTemplate.opsForValue().get(
                String.format(ID_FORMAT, id));
    }

    public Flux<ServiceData> findAll(){
        return reactiveRedisTemplate
                .keys(String.format(ID_FORMAT, "*"))
                .flatMap(key -> reactiveRedisTemplate.opsForValue().get(key));
    }
}
