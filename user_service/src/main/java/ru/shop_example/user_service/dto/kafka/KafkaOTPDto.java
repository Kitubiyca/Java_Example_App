package ru.shop_example.user_service.dto.kafka;

import lombok.*;

@Getter
@Setter(AccessLevel.PUBLIC)
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class KafkaOTPDto {

    private String name;
    private String email;
    private String code;
}
