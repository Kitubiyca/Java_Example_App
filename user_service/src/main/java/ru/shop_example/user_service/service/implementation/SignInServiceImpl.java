package ru.shop_example.user_service.service.implementation;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.shop_example.user_service.dto.RefreshTokenDto;
import ru.shop_example.user_service.dto.SignInDto;
import ru.shop_example.user_service.dto.SignInResponseDto;
import ru.shop_example.user_service.entity.User;
import ru.shop_example.user_service.entity.constant.UserStatus;
import ru.shop_example.user_service.exception.custom.AuthorizationFailedException;
import ru.shop_example.user_service.exception.custom.RequestDeniedException;
import ru.shop_example.user_service.exception.custom.UserNotFoundException;
import ru.shop_example.user_service.repository.UserRepository;
import ru.shop_example.user_service.service.SignInService;
import ru.shop_example.user_service.util.JwtUtils;

import java.time.Duration;
import java.time.OffsetDateTime;
import java.time.temporal.ChronoUnit;
import java.util.NoSuchElementException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class SignInServiceImpl implements SignInService {

    private final UserRepository userRepository;
    private final StringRedisTemplate stringRedisTemplate;
    private final BCryptPasswordEncoder passwordEncoder;
    private final JwtUtils jwtUtils;

    @Value("${token.access.ttl}")
    private long accessTokenTTL;
    @Value("${token.refresh.ttl}")
    private long refreshTokenTTL;

    @Transactional
    public SignInResponseDto signIn(SignInDto signInDto){
        log.info("Called signIn service method");
        User user = userRepository.findUserByEmail(signInDto.getEmail()).orElseThrow(() -> new UserNotFoundException(String.format("User with email %s not found", signInDto.getEmail())));
        if (!user.getStatus().equals(UserStatus.active)) throw new RequestDeniedException(String.format("User status is %s instead of %s", user.getStatus(), UserStatus.active));
        if (!passwordEncoder.matches(signInDto.getPassword(), user.getPassword())) throw new AuthorizationFailedException("Wrong password");
        user.setAccessDate(OffsetDateTime.now());
        return createSessionAndPrepareResponse(user.getId(), user.getRole().getName());
    }

    @Transactional
    public SignInResponseDto signInWithRefreshToken(RefreshTokenDto refreshTokenDto){
        log.info("Called signInWithRefreshToken service method");
        UUID userId;
        UUID sessionId;
        try{
            Claims claims = jwtUtils.validateToken(refreshTokenDto.getValue());
            sessionId = UUID.fromString((String) claims.get("sessionId"));
            userId = UUID.fromString(stringRedisTemplate.keys(String.format("token:refresh:*:%s", sessionId)).iterator().next().split(":")[2]);
        } catch (JwtException e){
            throw new AuthorizationFailedException("Invalid jwt provided");
        } catch (NoSuchElementException e){
            throw new AuthorizationFailedException("Expired jwt provided");
        }
        stringRedisTemplate.keys(String.format("token:*:%s:%s", userId, sessionId)).forEach(stringRedisTemplate::delete);
        User user = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException("Linked user not found"));
        if (!user.getStatus().equals(UserStatus.active)) throw new RequestDeniedException(String.format("User status is %s instead of %s", user.getStatus(), UserStatus.active));
        user.setAccessDate(OffsetDateTime.now());
        return createSessionAndPrepareResponse(user.getId(), user.getRole().getName());
    }

    private SignInResponseDto createSessionAndPrepareResponse(UUID userId, String userRole){
        UUID sessionId = UUID.randomUUID();
        String refreshToken = jwtUtils.generateRefreshToken(sessionId, userId, userRole);
        String accessToken = jwtUtils.generateAccessToken(sessionId, userId, userRole);
        stringRedisTemplate.opsForValue().set(
                String.format("token:refresh:%s:%s", userId, sessionId),
                refreshToken,
                Duration.of(refreshTokenTTL, ChronoUnit.SECONDS));
        stringRedisTemplate.opsForValue().set(
                String.format("token:access:%s:%s", userId, sessionId),
                accessToken,
                Duration.of(accessTokenTTL, ChronoUnit.SECONDS));
        return new SignInResponseDto(accessToken, refreshToken);
    }
}
