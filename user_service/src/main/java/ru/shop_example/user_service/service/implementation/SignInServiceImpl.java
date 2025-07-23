package ru.shop_example.user_service.service.implementation;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.shop_example.user_service.configuration.TokenProperties;
import ru.shop_example.user_service.dto.RequestRefreshTokenDto;
import ru.shop_example.user_service.dto.RequestSignInDto;
import ru.shop_example.user_service.dto.ResponseSignInDto;
import ru.shop_example.user_service.entity.User;
import ru.shop_example.user_service.entity.constant.UserStatus;
import ru.shop_example.user_service.exception.custom.AuthorizationFailedException;
import ru.shop_example.user_service.exception.custom.RequestDeniedException;
import ru.shop_example.user_service.exception.custom.UserNotFoundException;
import ru.shop_example.user_service.repository.SessionTokenRepository;
import ru.shop_example.user_service.repository.UserRepository;
import ru.shop_example.user_service.service.SignInService;
import ru.shop_example.user_service.util.JwtUtils;
import ru.shop_example.user_service.controller.SignInController;

import java.time.OffsetDateTime;
import java.util.NoSuchElementException;
import java.util.UUID;

/**
 * Реализация сервиса для обработки авторизации пользователя в приложении.
 *
 * @see SignInController
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class SignInServiceImpl implements SignInService {

    private final UserRepository userRepository;
    private final SessionTokenRepository sessionTokenRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final JwtUtils jwtUtils;
    private final TokenProperties tokenProperties;

    /**
     * {@inheritDoc}
     */
    @Transactional
    public ResponseSignInDto signIn(RequestSignInDto requestSignInDto){
        log.info("Called signIn service method");
        User user = userRepository.findUserByEmail(requestSignInDto.getEmail()).orElseThrow(() -> new UserNotFoundException(String.format("User with email %s not found", requestSignInDto.getEmail())));
        if (!user.getStatus().equals(UserStatus.active)) throw new RequestDeniedException(String.format("User status is %s instead of %s", user.getStatus(), UserStatus.active));
        if (!passwordEncoder.matches(requestSignInDto.getPassword(), user.getPassword())) throw new AuthorizationFailedException("Wrong password");
        user.setAccessDate(OffsetDateTime.now());
        return createSessionAndPrepareResponse(user.getId(), user.getRole().getName());
    }

    /**
     * {@inheritDoc}
     */
    @Transactional
    public ResponseSignInDto signInWithRefreshToken(RequestRefreshTokenDto requestRefreshTokenDto){
        log.info("Called signInWithRefreshToken service method");
        UUID userId;
        UUID sessionId;
        try{
            Claims claims = jwtUtils.validateToken(requestRefreshTokenDto.getRefreshToken());
            sessionId = UUID.fromString((String) claims.get("sessionId"));
            userId = UUID.fromString(sessionTokenRepository.findRefreshTokenBySessionId(sessionId).split(":")[2]);
        } catch (JwtException e){
            throw new AuthorizationFailedException("Invalid jwt provided");
        } catch (NoSuchElementException e){
            throw new AuthorizationFailedException("Expired jwt provided");
        }
        sessionTokenRepository.deleteAllTokensByUserIdAndSessionId(userId, sessionId);
        User user = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException("Linked user not found"));
        if (!user.getStatus().equals(UserStatus.active)) throw new RequestDeniedException(String.format("User status is %s instead of %s", user.getStatus(), UserStatus.active));
        user.setAccessDate(OffsetDateTime.now());
        return createSessionAndPrepareResponse(user.getId(), user.getRole().getName());
    }

    /**
     * Метод с общей логикой для работы с дто ответа.
     *
     * @param userId id пользователя
     * @param userRole роль пользователя
     *
     * @return дто ответа с access и refresh токенами для авторизации
     */
    private ResponseSignInDto createSessionAndPrepareResponse(UUID userId, String userRole){
        UUID sessionId = UUID.randomUUID();
        String refreshToken = jwtUtils.generateRefreshToken(sessionId, userId, userRole);
        String accessToken = jwtUtils.generateAccessToken(sessionId, userId, userRole);
        sessionTokenRepository.saveRefreshToken(userId, sessionId, refreshToken, tokenProperties.refresh().ttl());
        sessionTokenRepository.saveAccessToken(userId, sessionId, accessToken, tokenProperties.access().ttl());
        return new ResponseSignInDto(accessToken, refreshToken);
    }
}
