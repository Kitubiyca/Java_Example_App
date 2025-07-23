package ru.shop_example.user_service.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.config.EnableMongoAuditing;

/**
 * Конфигурация MongoDB.
 * <p>
 * Добавлена исключительно для аннотации @EnableMongoAuditing,
 * чтобы можно было автоматизировать обработку дат создания и изменения документов.
 */
@Configuration
@EnableMongoAuditing
public class MongoConfiguration {
}
