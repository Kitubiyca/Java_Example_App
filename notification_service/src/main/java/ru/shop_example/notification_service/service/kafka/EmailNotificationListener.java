package ru.shop_example.notification_service.service.kafka;

import org.springframework.validation.annotation.Validated;
import ru.shop_example.notification_service.dto.ConfirmationCodeDto;

public interface EmailNotificationListener {

    public void listenToEmailConfirmationCodeTopic(ConfirmationCodeDto confirmationCodeDto);
}
