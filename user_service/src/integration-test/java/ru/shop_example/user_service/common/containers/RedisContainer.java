package ru.shop_example.user_service.common.containers;

import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@Testcontainers
public interface RedisContainer {

    @Container
    @ServiceConnection
    com.redis.testcontainers.RedisContainer REDIS =
            new com.redis.testcontainers.RedisContainer("redis:7.4-alpine");
}
