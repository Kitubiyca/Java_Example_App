package ru.shop_example.gateway.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import ru.shop_example.gateway.entity.ServiceData;

@Configuration
public class RedisConfig {

    @Bean
    public ReactiveRedisTemplate<String, ServiceData> serviceDataReactiveRedisTemplate(LettuceConnectionFactory lettuceConnectionFactory) {

        Jackson2JsonRedisSerializer<ServiceData> valueSerializer = new Jackson2JsonRedisSerializer<>(ServiceData.class);

        RedisSerializationContext.RedisSerializationContextBuilder<String, ServiceData> builder = RedisSerializationContext.newSerializationContext(new StringRedisSerializer());

        RedisSerializationContext<String, ServiceData> context = builder
                .key(new StringRedisSerializer())
                .value(valueSerializer)
                .hashKey(new StringRedisSerializer())
                .hashValue(valueSerializer)
                .build();

        return new ReactiveRedisTemplate<>(lettuceConnectionFactory, context);
    }
}
