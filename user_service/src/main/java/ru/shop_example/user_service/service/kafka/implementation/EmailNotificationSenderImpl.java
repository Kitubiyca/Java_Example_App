package ru.shop_example.user_service.service.kafka.implementation;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import ru.shop_example.user_service.dto.kafka.KafkaOTPDto;
import ru.shop_example.user_service.service.kafka.EmailNotificationSender;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailNotificationSenderImpl implements EmailNotificationSender {

    private final KafkaTemplate<String, KafkaOTPDto> kafkaTemplate;

    public void sendToEmailOTPTopic(KafkaOTPDto kafkaOTPDto){
        log.info("Called sendToEmailOTPTopic kafka method");
        kafkaTemplate.send("email-otp-topic", kafkaOTPDto);
    }
}
