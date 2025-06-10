package ru.shop_example.notification_service.service.kafka;

import ru.shop_example.notification_service.dto.OTPDto;

public interface EmailNotificationListener {

    public void listenToEmailOTPTopic(OTPDto OTPDto);
}
