package ru.shop_example.gateway.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;

@RedisHash(value = "serviceData")
@AllArgsConstructor
@Getter
@Setter
@Builder
public class ServiceData {

    @Id
    private String name;
    @Indexed
    private Integer hash;
    private Integer routeCount;
}
