package ru.shop_example.user_service.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import ru.shop_example.user_service.configuration.TokenProperties;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.UUID;

/**
 * Утилиты для работы с JWT токенами.
 * Использует jjwt.
 * Подтягивает настройки для jwt и секретный ключ из конфигурации.
 *
 * @see TokenProperties
 */
@Component
@Slf4j
@RequiredArgsConstructor
public class JwtUtils {

    @Value("${secret.jwt.key}")
    private String secretKey;
    private final TokenProperties tokenProperties;

    /**
     * Создание access токена.
     *
     * @param sessionId id сессии
     * @param userId id пользователя
     * @param userRole роль пользователя
     *
     * @return строку с access токеном
     */
    public String generateAccessToken(UUID sessionId, UUID userId, String userRole){
        log.info("Called generateAccessToken utils method");
        return Jwts.builder()
                .claim("type", "access")
                .claim("sessionId", sessionId)
                .claim("userId", userId)
                .claim("userRole", userRole)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + tokenProperties.access().ttl().toMillis()))
                .signWith(useSecretKey(secretKey))
                .compact();
    }

    /**
     * Создание refresh токена.
     *
     * @param sessionId id сессии
     * @param userId id пользователя
     * @param userRole роль пользователя
     *
     * @return строку с refresh токеном
     */
    public String generateRefreshToken(UUID sessionId, UUID userId, String userRole){
        log.info("Called generateRefreshToken utils method");
        return Jwts.builder()
                .claim("type", "refresh")
                .claim("sessionId", sessionId)
                .claim("userId", userId)
                .claim("userRole", userRole)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + tokenProperties.refresh().ttl().toMillis()))
                .signWith(useSecretKey(secretKey))
                .compact();
    }

    /**
     * Валидация токена.
     *
     * @param token строка с токеном
     *
     * @return Claims этого токена
     */
    public Claims validateToken(String token){
        log.info("Called validateToken utils method");
        return Jwts.parser()
                .verifyWith(useSecretKey(secretKey))
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    /**
     * Простой метод для использования секретного ключа.
     *
     * @param key строка с ключом
     *
     * @return секретный ключ в виде объекта
     */
    private SecretKey useSecretKey(String key) {return Keys.hmacShaKeyFor(Decoders.BASE64.decode(key));}

}
