package ru.shop_example.notification_service.service;

import ru.shop_example.notification_service.dto.ConfirmationCodeDto;

public interface EmailNotificationService {

    public void sendConfirmationCode(ConfirmationCodeDto confirmationCodeDto);
}
