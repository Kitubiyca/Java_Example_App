package ru.shop_example.user_service.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.io.Encoders;
import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;
import ru.shop_example.user_service.configuration.TokenProperties;

import java.security.Key;
import java.time.Duration;
import java.util.Date;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class JwtUtilsTest {

    @Mock
    private TokenProperties tokenProperties;
    @Mock
    private TokenProperties.Access accessProperties;
    @Mock
    private TokenProperties.Refresh refreshProperties;
    private final Duration accessTTL = Duration.ofMinutes(30);
    private final Duration refreshTTL = Duration.ofDays(7);
    private String secretKey;
    @Spy
    @InjectMocks
    JwtUtils jwtUtils;

    @BeforeEach
    void setup(){
        Key signingKey = Keys.secretKeyFor(SignatureAlgorithm.HS256);
        secretKey = Encoders.BASE64.encode(signingKey.getEncoded());
    }

    @Test
    @DisplayName("Должен возвращать корректный access токен")
    void generateAccessTokenShouldReturnCorrectToken(){

        //Arrange
        UUID sessionId = UUID.randomUUID();
        UUID userId = UUID.randomUUID();
        String role = "CUSTOMER";

        when(tokenProperties.access()).thenReturn(accessProperties);
        when(accessProperties.ttl()).thenReturn(accessTTL);
        ReflectionTestUtils.setField(jwtUtils, "secretKey", secretKey);

        //Act
        String actual = jwtUtils.generateAccessToken(sessionId, userId, role);

        //Verify
        Claims claims = Jwts.parser()
                .verifyWith(Keys.hmacShaKeyFor(Decoders.BASE64.decode(secretKey)))
                .build()
                .parseSignedClaims(actual)
                .getPayload();

        assertEquals("access", claims.get("type"));
        assertEquals(sessionId.toString(), claims.get("sessionId"));
        assertEquals(userId.toString(), claims.get("userId"));
        assertEquals(role, claims.get("userRole"));
    }

    @Test
    @DisplayName("Должен возвращать корректный refresh токен")
    void generateRefreshTokenShouldReturnCorrectToken(){

        //Arrange
        UUID sessionId = UUID.randomUUID();
        UUID userId = UUID.randomUUID();
        String role = "CUSTOMER";

        when(tokenProperties.refresh()).thenReturn(refreshProperties);
        when(refreshProperties.ttl()).thenReturn(refreshTTL);
        ReflectionTestUtils.setField(jwtUtils, "secretKey", secretKey);

        //Act
        String actual = jwtUtils.generateRefreshToken(sessionId, userId, role);

        //Verify
        Claims claims = Jwts.parser()
                .verifyWith(Keys.hmacShaKeyFor(Decoders.BASE64.decode(secretKey)))
                .build()
                .parseSignedClaims(actual)
                .getPayload();

        assertEquals("refresh", claims.get("type"));
        assertEquals(sessionId.toString(), claims.get("sessionId"));
        assertEquals(userId.toString(), claims.get("userId"));
        assertEquals(role, claims.get("userRole"));
    }

    @Test
    @DisplayName("Должен возвращать корректный access токен")
    void validateTokenShouldReturnCorrectClaims(){

        //Arrange
        UUID sessionId = UUID.randomUUID();
        UUID userId = UUID.randomUUID();
        String role = "CUSTOMER";

        String token = Jwts.builder()
                .claim("type", "access")
                .claim("sessionId", sessionId)
                .claim("userId", userId)
                .claim("userRole", role)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + accessTTL.toMillis()))
                .signWith(Keys.hmacShaKeyFor(Decoders.BASE64.decode(secretKey)))
                .compact();

        ReflectionTestUtils.setField(jwtUtils, "secretKey", secretKey);

        //Act
        Claims claims = jwtUtils.validateToken(token);

        //Verify
        assertEquals("access", claims.get("type"));
        assertEquals(sessionId.toString(), claims.get("sessionId"));
        assertEquals(userId.toString(), claims.get("userId"));
        assertEquals(role, claims.get("userRole"));
    }
}
