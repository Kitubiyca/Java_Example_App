package ru.shop_example.user_service.service.kafka;

import ru.shop_example.user_service.dto.kafka.KafkaOTPDto;

/**
 * Продюсер для отправки сообщений пользователю по электронной почте через notification service.
 */
public interface EmailNotificationSender {

    /**
     * Отправка кода подтверждения. Топик email-otp-topic.
     *
     * @param kafkaOTPDto POJO с именем пользователя, почтой и 4-х значным кодом
     */
    void sendToEmailOTPTopic(KafkaOTPDto kafkaOTPDto);
}
