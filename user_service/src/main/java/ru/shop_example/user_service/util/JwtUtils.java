package ru.shop_example.user_service.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.UUID;

@Component
@Slf4j
public class JwtUtils {

    @Value("${secret.jwt.key}")
    private String secretKey;
    @Value("${token.access.ttl}")
    private long accessTokenTTL;
    @Value("${token.refresh.ttl}")
    private long refreshTokenTTL;

    public String generateAccessToken(UUID sessionId, UUID userId, String userRole){
        log.info("Called generateAccessToken utils method");
        return Jwts.builder()
                .claim("type", "access")
                .claim("sessionId", sessionId)
                .claim("userId", userId)
                .claim("userRole", userRole)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + accessTokenTTL * 1000))
                .signWith(useSecretKey(secretKey))
                .compact();
    }

    public String generateRefreshToken(UUID sessionId, UUID userId, String userRole){
        log.info("Called generateRefreshToken utils method");
        return Jwts.builder()
                .claim("type", "refresh")
                .claim("sessionId", sessionId)
                .claim("userId", userId)
                .claim("userRole", userRole)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + refreshTokenTTL * 1000))
                .signWith(useSecretKey(secretKey))
                .compact();
    }

    public Claims validateToken(String token){
        log.info("Called validateToken utils method");
        return Jwts.parser()
                .verifyWith(useSecretKey(secretKey))
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    private SecretKey useSecretKey(String key) {return Keys.hmacShaKeyFor(Decoders.BASE64.decode(key));}

}
