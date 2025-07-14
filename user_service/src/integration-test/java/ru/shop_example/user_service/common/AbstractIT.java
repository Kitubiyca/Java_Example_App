package ru.shop_example.user_service.common;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import ru.shop_example.user_service.common.containers.*;

@SpringBootTest
@ActiveProfiles("test")
public interface AbstractIT extends EurekaContainer, KafkaContainer, MongoContainer, PostgresContainer, RedisContainer {
}
