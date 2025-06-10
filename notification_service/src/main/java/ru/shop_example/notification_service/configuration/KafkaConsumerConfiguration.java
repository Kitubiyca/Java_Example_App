package ru.shop_example.notification_service.configuration;

import lombok.RequiredArgsConstructor;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import ru.shop_example.notification_service.dto.OTPDto;

@Configuration
@RequiredArgsConstructor
public class KafkaConsumerConfiguration {

    private final KafkaProperties kafkaProperties;

    @Bean
    public ConsumerFactory<String, OTPDto> otpDtoConsumerFactory() {
        return new DefaultKafkaConsumerFactory<>(
                kafkaProperties.buildConsumerProperties(),
                new StringDeserializer(),
                new JsonDeserializer<>(OTPDto.class, false)
        );
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, OTPDto> otpDtoContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, OTPDto> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(otpDtoConsumerFactory());
        return factory;
    }
}
