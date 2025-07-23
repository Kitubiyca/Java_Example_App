package ru.shop_example.product_service.dto;

import lombok.*;

import java.time.Instant;

@Getter
@Setter(AccessLevel.PUBLIC)
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class ErrorDto {

    private int errorCode;
    private String errorText;
    private String url;
    private String message;
    private final Instant timeStamp = Instant.now();
}
