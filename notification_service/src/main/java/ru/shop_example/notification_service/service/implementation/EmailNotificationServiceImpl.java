package ru.shop_example.notification_service.service.implementation;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Service;
import ru.shop_example.notification_service.dto.ConfirmationCodeDto;
import ru.shop_example.notification_service.service.EmailNotificationService;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailNotificationServiceImpl implements EmailNotificationService {

    private final MailSender mailSender;

    @Value("${mail.sender.name}")
    private String from;

    public void sendConfirmationCode(ConfirmationCodeDto confirmationCodeDto){
        log.info("Called sendConfirmationCode service method");
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(from);
        message.setTo(confirmationCodeDto.getEmail());
        message.setSubject("Код подтверждения в Example_App");
        message.setText(String.format("Здравствуйте, %s. Ваш код подтверждения: %s", confirmationCodeDto.getName(), confirmationCodeDto.getCode()));
        //mailSender.send(message);
        log.info("code is {}", confirmationCodeDto.getCode());
    }
}
