package ru.shop_example.user_service.util;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.shop_example.user_service.dto.kafka.KafkaOTPDto;
import ru.shop_example.user_service.entity.OTP;
import ru.shop_example.user_service.entity.User;
import ru.shop_example.user_service.entity.constant.Intent;
import ru.shop_example.user_service.service.kafka.EmailNotificationSender;

import java.security.SecureRandom;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class Utils {

    private final EmailNotificationSender emailNotificationSender;
    private final SecureRandom secureRandom = new SecureRandom();

    public void sendOTPWithEmail(User user, OTP otp){
        log.info("Called sendOTPWithEmail utils method");
        emailNotificationSender.sendToEmailOTPTopic(new KafkaOTPDto(user.getFirstname(), user.getEmail(), otp.getValue()));
    }

    public OTP createOTP (UUID userId, Intent intent){
        return new OTP(UUID.randomUUID(), userId, intent, String.format("%04d", secureRandom.nextInt(10000)));
    }
}
