package ru.shop_example.gateway.dto;

import lombok.*;
import org.springframework.http.HttpStatus;

import java.time.Instant;

@Getter
@Setter(AccessLevel.PUBLIC)
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class ErrorDto {
    private HttpStatus errorCode;
    private String errorText;
    private String url;
    private String message;
    private final Instant timeStamp = Instant.now();
}
