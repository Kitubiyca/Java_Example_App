package ru.shop_example.user_service.outbox;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.shop_example.user_service.entity.OTP;
import ru.shop_example.user_service.entity.constant.Intent;

import java.time.OffsetDateTime;
import java.util.Optional;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class OtpOutboxRepository {
    private final JdbcTemplate jdbc;

    public void save(OTP otp, String name, String email) {
        jdbc.update("""
                INSERT INTO otp_outbox (id, user_id, intent, code, recipient_name, email, expires_at)
                VALUES (?, ?, ?, ?, ?, ?, ?)
                """, otp.getId(), otp.getUserId(), otp.getIntent().name(), otp.getValue(), name, email,
                OffsetDateTime.now().plusMinutes(5));
    }

    // The caller keeps this row locked until both delivery attempts and status updates finish.
    public Optional<Message> lockNext() {
        return jdbc.query("""
                SELECT * FROM otp_outbox
                WHERE (redis_status = 'PENDING' OR kafka_status = 'PENDING')
                  AND next_attempt_at <= CURRENT_TIMESTAMP
                ORDER BY created_at, id
                LIMIT 1 FOR UPDATE SKIP LOCKED
                """, (rs, row) -> new Message(
                new OTP(rs.getObject("id", UUID.class), rs.getObject("user_id", UUID.class),
                        Intent.valueOf(rs.getString("intent")), rs.getString("code")),
                rs.getString("recipient_name"), rs.getString("email"),
                rs.getObject("expires_at", OffsetDateTime.class),
                rs.getString("redis_status"), rs.getString("kafka_status"))).stream().findFirst();
    }

    public void update(Message message, String redisStatus, String kafkaStatus, String error) {
        jdbc.update("""
                UPDATE otp_outbox SET redis_status = ?, kafka_status = ?,
                  attempts = attempts + 1, last_error = ?, next_attempt_at = CURRENT_TIMESTAMP + INTERVAL '5 seconds'
                WHERE id = ?
                """, redisStatus, kafkaStatus, error, message.otp().getId());
    }

    public record Message(OTP otp, String name, String email, OffsetDateTime expiresAt,
                          String redisStatus, String kafkaStatus) {}
}
