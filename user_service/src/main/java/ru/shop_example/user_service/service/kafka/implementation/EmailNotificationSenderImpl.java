package ru.shop_example.user_service.service.kafka.implementation;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import ru.shop_example.user_service.dto.kafka.KafkaOTPDto;
import ru.shop_example.user_service.service.kafka.EmailNotificationSender;

/**
 * Продюсер для отправки сообщений пользователю по электронной почте через notification service.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class EmailNotificationSenderImpl implements EmailNotificationSender {

    private final KafkaTemplate<String, KafkaOTPDto> kafkaTemplate;

    /**
     * {@inheritDoc}
     */
    public void sendToEmailOTPTopic(KafkaOTPDto kafkaOTPDto){
        log.info("Called sendToEmailOTPTopic kafka method");
        try {
            kafkaTemplate.send("email-otp-topic", kafkaOTPDto).get(10, java.util.concurrent.TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new IllegalStateException("Kafka delivery interrupted", e);
        } catch (java.util.concurrent.ExecutionException | java.util.concurrent.TimeoutException e) {
            throw new IllegalStateException("Kafka delivery was not confirmed", e);
        }
    }
}
