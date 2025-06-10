package ru.shop_example.user_service.service.kafka;

import ru.shop_example.user_service.dto.kafka.KafkaOTPDto;

public interface EmailNotificationSender {
    void sendToEmailOTPTopic(KafkaOTPDto kafkaOTPDto);
}
