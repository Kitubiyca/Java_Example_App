package ru.shop_example.user_service.common.containers;

import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@Testcontainers
public interface EurekaContainer {

    @Container
    GenericContainer<?> EUREKA =
            new GenericContainer<>("steeltoeoss/eureka-server:4.1.1")
                    .withExposedPorts(8761);

    @DynamicPropertySource
    static void setProps(DynamicPropertyRegistry registry) {
        String host = EUREKA.getHost();
        Integer port = EUREKA.getFirstMappedPort();
        String url  = String.format("http://%s:%d/eureka/", host, port);
        registry.add("eureka.client.serviceUrl.defaultZone", () -> url);
    }
}
