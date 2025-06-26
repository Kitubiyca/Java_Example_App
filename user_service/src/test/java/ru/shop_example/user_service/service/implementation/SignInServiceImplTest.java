package ru.shop_example.user_service.service.implementation;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import ru.shop_example.user_service.configuration.TokenProperties;
import ru.shop_example.user_service.dto.RequestRefreshTokenDto;
import ru.shop_example.user_service.dto.RequestSignInDto;
import ru.shop_example.user_service.dto.ResponseSignInDto;
import ru.shop_example.user_service.entity.Role;
import ru.shop_example.user_service.entity.User;
import ru.shop_example.user_service.entity.constant.UserStatus;
import ru.shop_example.user_service.exception.custom.AuthorizationFailedException;
import ru.shop_example.user_service.exception.custom.RequestDeniedException;
import ru.shop_example.user_service.exception.custom.UserNotFoundException;
import ru.shop_example.user_service.repository.SessionTokenRepository;
import ru.shop_example.user_service.repository.UserRepository;
import ru.shop_example.user_service.util.JwtUtils;

import java.time.Duration;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class SignInServiceImplTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private SessionTokenRepository sessionTokenRepository;
    @Mock
    private BCryptPasswordEncoder passwordEncoder;
    @Mock
    private JwtUtils jwtUtils;
    @Mock
    private TokenProperties tokenProperties;
    @Mock
    private TokenProperties.Access accessProperties;
    @Mock
    private TokenProperties.Refresh refreshProperties;
    @Spy
    @InjectMocks
    private SignInServiceImpl signInService;
    private final Duration accessTTL = Duration.ofMinutes(30);
    private final Duration refreshTTL = Duration.ofDays(7);
    @Mock
    Claims mockClaims;

    @Test
    @DisplayName("Должен успешно выполнять вход")
    void signInShouldSignInSuccessfully(){

        //Arrange
        RequestSignInDto requestSignInDto = createRequestSignInDto();
        User user = createActiveUser();
        ResponseSignInDto expected = createResponseSignInDto();

        when(userRepository.findUserByEmail(requestSignInDto.getEmail())).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(requestSignInDto.getPassword(), user.getPassword())).thenReturn(true);

        when(jwtUtils.generateAccessToken(any(UUID.class), eq(user.getId()), eq(user.getRole().getName()))).thenReturn(expected.getAccessToken());
        when(jwtUtils.generateRefreshToken(any(UUID.class), eq(user.getId()), eq(user.getRole().getName()))).thenReturn(expected.getRefreshToken());

        when(tokenProperties.access()).thenReturn(accessProperties);
        when(tokenProperties.refresh()).thenReturn(refreshProperties);

        when(accessProperties.ttl()).thenReturn(accessTTL);
        when(refreshProperties.ttl()).thenReturn(refreshTTL);

        //Act
        ResponseSignInDto actual = signInService.signIn(requestSignInDto);

        //Verify
        assertEquals(expected, actual);
        verify(sessionTokenRepository).saveAccessToken(eq(user.getId()), any(UUID.class), eq(expected.getAccessToken()), eq(accessTTL));
        verify(sessionTokenRepository).saveRefreshToken(eq(user.getId()), any(UUID.class), eq(expected.getRefreshToken()), eq(refreshTTL));
    }

    @Test
    @DisplayName("Должен выбрасывать ошибку если пользователь не найден")
    void signInShouldThrowUserNotFoundException(){

        //Arrange
        RequestSignInDto requestSignInDto = createRequestSignInDto();

        when(userRepository.findUserByEmail(requestSignInDto.getEmail())).thenReturn(Optional.empty());

        //Act
        UserNotFoundException actual = assertThrows(
                UserNotFoundException.class,
                () -> signInService.signIn(requestSignInDto));

        //Verify
        assertEquals(String.format("User with email %s not found", requestSignInDto.getEmail()), actual.getMessage());
    }

    @Test
    @DisplayName("Должен выбрасывать ошибку если статус пользователя не равен активному")
    void signInShouldThrowRequestDeniedException(){

        //Arrange
        RequestSignInDto requestSignInDto = createRequestSignInDto();
        User user = createActiveUser();
        user.setStatus(UserStatus.pending);

        when(userRepository.findUserByEmail(requestSignInDto.getEmail())).thenReturn(Optional.of(user));

        //Act
        RequestDeniedException actual = assertThrows(
                RequestDeniedException.class,
                () -> signInService.signIn(requestSignInDto));

        //Verify
        assertEquals(String.format("User status is %s instead of %s", user.getStatus(), UserStatus.active), actual.getMessage());
    }

    @Test
    @DisplayName("Должен выбрасывать ошибку при несовпадении пароля")
    void signInShouldThrowAuthorizationFailedException(){

        //Arrange
        RequestSignInDto requestSignInDto = createRequestSignInDto();
        User user = createActiveUser();

        when(userRepository.findUserByEmail(requestSignInDto.getEmail())).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(requestSignInDto.getPassword(), user.getPassword())).thenReturn(false);

        //Act
        AuthorizationFailedException actual = assertThrows(
                AuthorizationFailedException.class,
                () -> signInService.signIn(requestSignInDto));

        //Verify
        assertEquals("Wrong password", actual.getMessage());
    }

    @Test
    @DisplayName("Должен успешно выполнять вход через refresh токен")
    void signInWithRefreshTokenShouldSignInSuccessfully(){

        //Arrange
        RequestRefreshTokenDto requestRefreshTokenDto = new RequestRefreshTokenDto();
        requestRefreshTokenDto.setValue("token");
        User user = createActiveUser();
        ResponseSignInDto expected = createResponseSignInDto();
        UUID sessionId = UUID.randomUUID();

        when(jwtUtils.validateToken(requestRefreshTokenDto.getValue())).thenReturn(mockClaims);
        when(mockClaims.get("sessionId")).thenReturn(sessionId.toString());
        when(sessionTokenRepository.findRefreshTokenBySessionId(sessionId)).thenReturn(String.format("token:refresh:%s:%s", user.getId(), sessionId));

        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));

        when(jwtUtils.generateAccessToken(any(UUID.class), eq(user.getId()), eq(user.getRole().getName()))).thenReturn(expected.getAccessToken());
        when(jwtUtils.generateRefreshToken(any(UUID.class), eq(user.getId()), eq(user.getRole().getName()))).thenReturn(expected.getRefreshToken());

        when(tokenProperties.access()).thenReturn(accessProperties);
        when(tokenProperties.refresh()).thenReturn(refreshProperties);

        when(accessProperties.ttl()).thenReturn(accessTTL);
        when(refreshProperties.ttl()).thenReturn(refreshTTL);

        //Act
        ResponseSignInDto actual = signInService.signInWithRefreshToken(requestRefreshTokenDto);

        //Verify
        assertEquals(expected, actual);
        verify(sessionTokenRepository).deleteAllTokensByUserIdAndSessionId(eq(user.getId()), eq(sessionId));
        verify(sessionTokenRepository).saveAccessToken(eq(user.getId()), any(UUID.class), eq(expected.getAccessToken()), eq(accessTTL));
        verify(sessionTokenRepository).saveRefreshToken(eq(user.getId()), any(UUID.class), eq(expected.getRefreshToken()), eq(refreshTTL));
    }

    @Test
    @DisplayName("Должен выбрасывать ошибку если получает некорректный токен")
    void signInWithRefreshTokenShouldThrowAuthorizationFailedException1(){

        //Arrange
        RequestRefreshTokenDto requestRefreshTokenDto = new RequestRefreshTokenDto();
        requestRefreshTokenDto.setValue("token");

        when(jwtUtils.validateToken(requestRefreshTokenDto.getValue())).thenThrow(new JwtException("error"));

        //Act
        AuthorizationFailedException actual = assertThrows(
                AuthorizationFailedException.class,
                () -> signInService.signInWithRefreshToken(requestRefreshTokenDto));

        //Verify
        assertEquals("Invalid jwt provided", actual.getMessage());
    }

    @Test
    @DisplayName("Должен выбрасывать ошибку если токен не найден среди активных")
    void signInWithRefreshTokenShouldThrowAuthorizationFailedException2(){

        //Arrange
        RequestRefreshTokenDto requestRefreshTokenDto = new RequestRefreshTokenDto();
        requestRefreshTokenDto.setValue("token");
        UUID sessionId = UUID.randomUUID();

        when(jwtUtils.validateToken(requestRefreshTokenDto.getValue())).thenReturn(mockClaims);
        when(mockClaims.get("sessionId")).thenReturn(sessionId.toString());
        when(sessionTokenRepository.findRefreshTokenBySessionId(sessionId)).thenThrow(new NoSuchElementException("error"));

        //Act
        AuthorizationFailedException actual = assertThrows(
                AuthorizationFailedException.class,
                () -> signInService.signInWithRefreshToken(requestRefreshTokenDto));

        //Verify
        assertEquals("Expired jwt provided", actual.getMessage());
    }

    @Test
    @DisplayName("Должен выбрасывать ошибку если пользователь не найден")
    void signInWithRefreshTokenShouldThrowUserNotFoundException(){

        //Arrange
        RequestRefreshTokenDto requestRefreshTokenDto = new RequestRefreshTokenDto();
        requestRefreshTokenDto.setValue("token");
        User user = createActiveUser();
        UUID sessionId = UUID.randomUUID();

        when(jwtUtils.validateToken(requestRefreshTokenDto.getValue())).thenReturn(mockClaims);
        when(mockClaims.get("sessionId")).thenReturn(sessionId.toString());

        when(sessionTokenRepository.findRefreshTokenBySessionId(sessionId)).thenReturn(String.format("token:refresh:%s:%s", user.getId(), sessionId));
        when(userRepository.findById(user.getId())).thenReturn(Optional.empty());

        //Act
        UserNotFoundException actual = assertThrows(
                UserNotFoundException.class,
                () -> signInService.signInWithRefreshToken(requestRefreshTokenDto));

        //Verify
        assertEquals("Linked user not found", actual.getMessage());
        verify(sessionTokenRepository).deleteAllTokensByUserIdAndSessionId(eq(user.getId()), eq(sessionId));
    }

    @Test
    @DisplayName("Должен выбрасывать ошибку если статус пользователя не равен активному")
    void signInWithRefreshTokenShouldThrowRequestDeniedException(){

        //Arrange
        RequestRefreshTokenDto requestRefreshTokenDto = new RequestRefreshTokenDto();
        requestRefreshTokenDto.setValue("token");
        User user = createActiveUser();
        user.setStatus(UserStatus.suspended);
        UUID sessionId = UUID.randomUUID();

        when(jwtUtils.validateToken(requestRefreshTokenDto.getValue())).thenReturn(mockClaims);
        when(mockClaims.get("sessionId")).thenReturn(sessionId.toString());

        when(sessionTokenRepository.findRefreshTokenBySessionId(sessionId)).thenReturn(String.format("token:refresh:%s:%s", user.getId(), sessionId));
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));

        //Act
        RequestDeniedException actual = assertThrows(
                RequestDeniedException.class,
                () -> signInService.signInWithRefreshToken(requestRefreshTokenDto));

        //Verify
        assertEquals(String.format("User status is %s instead of %s", user.getStatus(), UserStatus.active), actual.getMessage());
        verify(sessionTokenRepository).deleteAllTokensByUserIdAndSessionId(eq(user.getId()), eq(sessionId));
    }

    private User createActiveUser(){
        return User.builder()
                .id(UUID.randomUUID())
                .email("example@example.com")
                .password("1234")
                .firstname("Иванович")
                .lastname("Иванов")
                .patronymic("Иванович")
                .phoneNumber("123456789012")
                .birthDate(LocalDate.parse("1990-01-01"))
                .accessDate(OffsetDateTime.now())
                .registrationDate(OffsetDateTime.now())
                .status(UserStatus.active)
                .role(createRole())
                .build();
    }

    private Role createRole(){
        return Role.builder()
                .id(UUID.randomUUID())
                .name("CUSTOMER")
                .description("Role desc")
                .build();
    }

    private RequestSignInDto createRequestSignInDto(){
        RequestSignInDto requestSignInDto = new RequestSignInDto();
        requestSignInDto.setEmail("example@example.com");
        requestSignInDto.setPassword("1234");
        return requestSignInDto;
    }

    private ResponseSignInDto createResponseSignInDto(){
        return ResponseSignInDto.builder()
                .accessToken("accessToken")
                .refreshToken("refreshToken")
                .build();
    }
}
