package ru.shop_example.gateway.entity;

import lombok.*;
import org.springframework.data.redis.core.RedisHash;

@RedisHash(value = "serviceData")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class ServiceData {

    private String name;
    private Integer hash;
    private Integer routeCount;
}
