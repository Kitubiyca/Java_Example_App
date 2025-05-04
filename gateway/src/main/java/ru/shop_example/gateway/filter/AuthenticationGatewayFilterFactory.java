package ru.shop_example.gateway.filter;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.data.redis.core.ReactiveStringRedisTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;
import ru.shop_example.gateway.exception.custom.JWTAuthorizationFailedException;

import javax.crypto.SecretKey;
import java.util.UUID;

@Component
public class AuthenticationGatewayFilterFactory extends AbstractGatewayFilterFactory<AuthenticationGatewayFilterFactory.Config> {

    private final ReactiveStringRedisTemplate reactiveStringRedisTemplate;

    @Value("${secret.jwt.key}")
    private String secretKey;

    public AuthenticationGatewayFilterFactory(ReactiveStringRedisTemplate reactiveStringRedisTemplate) {
        super(Config.class);
        this.reactiveStringRedisTemplate = reactiveStringRedisTemplate;
    }

    @Override
    public GatewayFilter apply(AuthenticationGatewayFilterFactory.Config config) {
        return (exchange, chain) -> {
            String rawToken = exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
            if (rawToken == null) return Mono.error(new JWTAuthorizationFailedException("No token detected"));
            if (!rawToken.startsWith("Bearer ")) return Mono.error(new JWTAuthorizationFailedException("Invalid header format"));

            String token = rawToken.substring(7);

            Claims claims;
            try{
                claims = Jwts.parser()
                        .require("type", "access")
                        .verifyWith(useSecretKey(secretKey))
                        .build()
                        .parseSignedClaims(token)
                        .getPayload();
            } catch (MissingClaimException mce) {
                return Mono.error(new JWTAuthorizationFailedException("No token type specified"));
            } catch (IncorrectClaimException ice) {
                return Mono.error(new JWTAuthorizationFailedException("Invalid token type"));
            } catch (JwtException jwtE) {
                return Mono.error(new JWTAuthorizationFailedException("Token validation failed"));
            }

            UUID sessionId = UUID.fromString((String) claims.get("sessionId"));
            UUID userId = UUID.fromString((String) claims.get("userId"));
            String userRole = (String) claims.get("userRole");
            if (sessionId == null || userId == null || userRole == null) return Mono.error(new JWTAuthorizationFailedException("Token missing authorization data"));

            return reactiveStringRedisTemplate.opsForValue()
                    .get(String.format("token:access:%s:%s", userId, sessionId))
                    .switchIfEmpty(Mono.error(new JWTAuthorizationFailedException("Provided token is no longer valid")))
                    .flatMap(redisToken -> {
                        if (!token.equals(redisToken)) return Mono.error(new JWTAuthorizationFailedException("Session token comparison error"));;
                        exchange.getAttributes().put("userId", userId);
                        exchange.getAttributes().put("userRole", userRole);
                        exchange.getAttributes().put("sessionId", sessionId);
                        return chain.filter(exchange.mutate().request(exchange.getRequest().mutate()
                                .header("user-id", userId.toString())
                                .header("user-role", userRole)
                                .header("session-id", sessionId.toString())
                                .build()).build());
                    });
        };
    }

    private SecretKey useSecretKey(String key) {return Keys.hmacShaKeyFor(Decoders.BASE64.decode(key));}

    public static class Config{
    }
}
