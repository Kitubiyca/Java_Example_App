package ru.shop_example.notification_service.service.kafka.implementation;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;
import ru.shop_example.notification_service.dto.ConfirmationCodeDto;
import ru.shop_example.notification_service.service.EmailNotificationService;
import ru.shop_example.notification_service.service.kafka.EmailNotificationListener;

@Component
@Slf4j
@RequiredArgsConstructor
public class EmailNotificationListenerImpl implements EmailNotificationListener {

    private final EmailNotificationService emailNotificationService;

    @KafkaListener(topics = "email-confirmation-code-topic", containerFactory = "confirmationCodeDtoContainerFactory")
    public void listenToEmailConfirmationCodeTopic(@Validated ConfirmationCodeDto confirmationCodeDto){
        log.info("Called listenToEmailConfirmationCodeTopic kafka method");
        emailNotificationService.sendConfirmationCode(confirmationCodeDto);
    }
}
