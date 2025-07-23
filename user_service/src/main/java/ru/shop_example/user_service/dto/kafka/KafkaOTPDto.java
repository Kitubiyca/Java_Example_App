package ru.shop_example.user_service.dto.kafka;

import lombok.*;
import ru.shop_example.user_service.service.kafka.EmailNotificationSender;

/**
 * Дто сообщения с кодом подтверждения в кафке.
 *
 * @see EmailNotificationSender
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class KafkaOTPDto {

    /** Как обращаться к пользователю */
    private String name;

    /** Электронная почта пользователя */
    private String email;

    /** Сам код подтверждения */
    private String code;
}
