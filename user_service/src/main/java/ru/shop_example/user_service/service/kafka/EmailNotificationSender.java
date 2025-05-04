package ru.shop_example.user_service.service.kafka;

import ru.shop_example.user_service.dto.kafka.KafkaConfirmationCodeDto;

public interface EmailNotificationSender {
    void sendToEmailConfirmationCodeTopic(KafkaConfirmationCodeDto kafkaConfirmationCodeDto);
}
