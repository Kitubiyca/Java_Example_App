package ru.shop_example.user_service.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import ru.shop_example.user_service.entity.redis.RedisOTP;

/**
 * Класс конфигурации для редис темплейта.
 */
@Configuration
public class RedisTemplateConfiguration {

    /**
     * Создаёт редис темплейт для работы с объектами {@link RedisOTP RedisOTP}.
     * Использует фабрику соединений Lettuce.
     * Ключ сериализует строкой, а значение JSONом.
     *
     * @param lettuceConnectionFactory фабрика соединений
     *
     * @return темплейт для работы с {@link RedisOTP RedisOTP}
     */
    @Bean
    public RedisTemplate<String, RedisOTP> redisOTPRedisTemplate(LettuceConnectionFactory lettuceConnectionFactory) {
        RedisTemplate<String, RedisOTP> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(lettuceConnectionFactory);
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setValueSerializer(new Jackson2JsonRedisSerializer<>(RedisOTP.class));
        return redisTemplate;
    }
}
