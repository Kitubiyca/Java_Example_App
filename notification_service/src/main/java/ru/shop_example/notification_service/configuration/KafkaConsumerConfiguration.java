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
import ru.shop_example.notification_service.dto.ConfirmationCodeDto;

@Configuration
@RequiredArgsConstructor
public class KafkaConsumerConfiguration {

    private final KafkaProperties kafkaProperties;

    @Bean
    public ConsumerFactory<String, ConfirmationCodeDto> confirmationCodeDtoConsumerFactory() {
        return new DefaultKafkaConsumerFactory<>(
                kafkaProperties.buildConsumerProperties(),
                new StringDeserializer(),
                new JsonDeserializer<>(ConfirmationCodeDto.class, false)
        );
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, ConfirmationCodeDto> confirmationCodeDtoContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, ConfirmationCodeDto> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(confirmationCodeDtoConsumerFactory());
        return factory;
    }
}
