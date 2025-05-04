package ru.shop_example.user_service.service.kafka.implementation;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import ru.shop_example.user_service.dto.kafka.KafkaConfirmationCodeDto;
import ru.shop_example.user_service.service.kafka.EmailNotificationSender;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailNotificationSenderImpl implements EmailNotificationSender {

    private final KafkaTemplate<String, KafkaConfirmationCodeDto> kafkaTemplate;

    public void sendToEmailConfirmationCodeTopic(KafkaConfirmationCodeDto kafkaConfirmationCodeDto){
        log.info("Called sendToEmailConfirmationCodeTopic kafka method");
        kafkaTemplate.send("email-confirmation-code-topic", kafkaConfirmationCodeDto);
    }
}
