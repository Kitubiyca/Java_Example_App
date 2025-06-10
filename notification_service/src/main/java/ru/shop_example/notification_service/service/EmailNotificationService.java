package ru.shop_example.notification_service.service;

import ru.shop_example.notification_service.dto.OTPDto;

public interface EmailNotificationService {

    public void sendOTP(OTPDto OTPDto);
}
