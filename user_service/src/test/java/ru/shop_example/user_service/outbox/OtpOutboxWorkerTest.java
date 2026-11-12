package ru.shop_example.user_service.outbox;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionTemplate;
import ru.shop_example.user_service.entity.OTP;
import ru.shop_example.user_service.entity.constant.Intent;
import ru.shop_example.user_service.repository.OTPRepository;
import ru.shop_example.user_service.service.kafka.EmailNotificationSender;

import java.time.OffsetDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class OtpOutboxWorkerTest {
    private final OtpOutboxRepository outbox = mock(OtpOutboxRepository.class);
    private final OTPRepository redis = mock(OTPRepository.class);
    private final EmailNotificationSender sender = mock(EmailNotificationSender.class);
    private final PlatformTransactionManager manager = mock(PlatformTransactionManager.class);
    private final OtpOutboxWorker worker = new OtpOutboxWorker(outbox, redis, sender, new TransactionTemplate(manager));

    @BeforeEach
    void setUp() {
        when(manager.getTransaction(any())).thenReturn(mock(TransactionStatus.class));
    }

    private OtpOutboxRepository.Message message(String redisStatus, OffsetDateTime expiresAt) {
        return new OtpOutboxRepository.Message(new OTP(UUID.randomUUID(), UUID.randomUUID(), Intent.signUp, "1234"),
                "Name", "test@example.com", expiresAt, redisStatus, "PENDING");
    }

    private void enqueue(OtpOutboxRepository.Message message) {
        when(outbox.lockNext()).thenReturn(Optional.of(message), Optional.empty());
    }

    @Test
    void sendsToRedisBeforeKafkaAndCommitsBothStatuses() {
        var message = message("PENDING", OffsetDateTime.now().plusMinutes(5));
        enqueue(message);
        worker.poll();
        var order = inOrder(redis, sender, outbox);
        order.verify(redis).set(eq(message.otp()), any());
        order.verify(sender).sendToEmailOTPTopic(any());
        order.verify(outbox).update(message, "SENT", "SENT", null);
        verify(manager, times(2)).commit(any());
    }

    @Test
    void redisFailurePreventsEmailAndLeavesBothPending() {
        var message = message("PENDING", OffsetDateTime.now().plusMinutes(5));
        enqueue(message);
        doThrow(new IllegalStateException()).when(redis).set(any(), any());
        worker.poll();
        verifyNoInteractions(sender);
        verify(outbox).update(message, "PENDING", "PENDING", "IllegalStateException");
    }

    @Test
    void kafkaFailurePreservesSuccessfulRedisStatus() {
        var message = message("PENDING", OffsetDateTime.now().plusMinutes(5));
        enqueue(message);
        doThrow(new IllegalStateException()).when(sender).sendToEmailOTPTopic(any());
        worker.poll();
        verify(outbox).update(message, "SENT", "PENDING", "IllegalStateException");
    }

    @Test
    void kafkaRetryDoesNotRewriteRedis() {
        var message = message("SENT", OffsetDateTime.now().plusMinutes(5));
        enqueue(message);
        worker.poll();
        verifyNoInteractions(redis);
        verify(sender).sendToEmailOTPTopic(any());
        verify(outbox).update(message, "SENT", "SENT", null);
    }

    @Test
    void expiredCodeIsNotDelivered() {
        var message = message("PENDING", OffsetDateTime.now().minusSeconds(1));
        enqueue(message);
        worker.poll();
        verifyNoInteractions(redis, sender);
        verify(outbox).update(message, "EXPIRED", "EXPIRED", null);
    }
}
