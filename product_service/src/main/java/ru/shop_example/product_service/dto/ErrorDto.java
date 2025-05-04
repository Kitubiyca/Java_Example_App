package ru.shop_example.product_service.dto;

import lombok.*;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

@Getter
@Setter(AccessLevel.PUBLIC)
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class ErrorDto {

    private HttpStatus errorCode;
    private String errorText;
    private String url;
    private String message;
    private final LocalDateTime timeStamp = LocalDateTime.now();
}
