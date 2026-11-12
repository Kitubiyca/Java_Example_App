package ru.shop_example.user_service.outbox;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.support.TransactionTemplate;
import ru.shop_example.user_service.dto.kafka.KafkaOTPDto;
import ru.shop_example.user_service.repository.OTPRepository;
import ru.shop_example.user_service.service.kafka.EmailNotificationSender;

import java.time.Duration;
import java.time.OffsetDateTime;

@Component
@RequiredArgsConstructor
@Slf4j
@ConditionalOnProperty(name = "outbox.enabled", havingValue = "true", matchIfMissing = true)
public class OtpOutboxWorker {
    private final OtpOutboxRepository outbox;
    private final OTPRepository otpRepository;
    private final EmailNotificationSender sender;
    private final TransactionTemplate transactionTemplate;

    @Scheduled(fixedDelayString = "${outbox.poll-delay-ms:1000}", initialDelayString = "${outbox.initial-delay-ms:1000}")
    public void poll() {
        // Bound each pass so shutdown and other scheduled tasks can proceed.
        for (int i = 0; i < 50; i++) {
            try {
                Boolean processed = transactionTemplate.execute(status -> outbox.lockNext().map(message -> {
                    deliver(message);
                    return true;
                }).orElse(false));
                if (!Boolean.TRUE.equals(processed)) return;
            } catch (RuntimeException e) {
                log.warn("OTP outbox transaction failed; will retry", e);
                return;
            }
        }
    }

    private void deliver(OtpOutboxRepository.Message message) {
        String redisStatus = message.redisStatus();
        String kafkaStatus = message.kafkaStatus();
        String error = null;
        Duration remaining = Duration.between(OffsetDateTime.now(), message.expiresAt());
        if (remaining.isNegative() || remaining.isZero()) {
            outbox.update(message, "PENDING".equals(redisStatus) ? "EXPIRED" : redisStatus,
                    "PENDING".equals(kafkaStatus) ? "EXPIRED" : kafkaStatus, null);
            return;
        }
        try {
            if ("PENDING".equals(redisStatus)) {
                otpRepository.set(message.otp(), remaining);
                redisStatus = "SENT";
            }
            // Send the email only after the code is available for confirmation.
            if ("PENDING".equals(kafkaStatus)) {
                sender.sendToEmailOTPTopic(new KafkaOTPDto(message.name(), message.email(), message.otp().getValue()));
                kafkaStatus = "SENT";
            }
        } catch (RuntimeException e) {
            error = e.getClass().getSimpleName();
            log.warn("OTP outbox delivery failed for {}; will retry ({})", message.otp().getId(), error);
        }
        outbox.update(message, redisStatus, kafkaStatus, error);
    }
}
