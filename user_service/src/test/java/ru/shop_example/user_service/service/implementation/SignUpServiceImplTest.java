package ru.shop_example.user_service.service.implementation;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import ru.shop_example.user_service.dto.RequestEmailDto;
import ru.shop_example.user_service.dto.RequestOTPDto;
import ru.shop_example.user_service.dto.RequestSignUpDto;
import ru.shop_example.user_service.dto.ResponseOTPIdDto;
import ru.shop_example.user_service.dto.kafka.KafkaOTPDto;
import ru.shop_example.user_service.entity.OTP;
import ru.shop_example.user_service.entity.Role;
import ru.shop_example.user_service.entity.User;
import ru.shop_example.user_service.entity.constant.Intent;
import ru.shop_example.user_service.entity.constant.UserStatus;
import ru.shop_example.user_service.exception.custom.*;
import ru.shop_example.user_service.mapper.UserMapper;
import ru.shop_example.user_service.repository.OTPRepository;
import ru.shop_example.user_service.repository.RoleRepository;
import ru.shop_example.user_service.repository.UserRepository;
import ru.shop_example.user_service.service.kafka.EmailNotificationSender;
import ru.shop_example.user_service.util.OTPUtils;

import java.time.Duration;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class SignUpServiceImplTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private RoleRepository roleRepository;
    @Mock
    private OTPRepository otpRepository;
    @Mock
    private BCryptPasswordEncoder passwordEncoder;
    @Mock
    private EmailNotificationSender emailNotificationSender;
    @Mock
    private UserMapper userMapper;
    @Mock
    private OTPUtils otpUtils;
    @Spy
    @InjectMocks
    private SignUpServiceImpl signUpService;

    @Test
    @DisplayName("Должен корректно сохранять пользователя")
    void registerUserShouldRegisterSuccessfully(){

        //Arrange
        RequestSignUpDto requestSignUpDto = createRequestSignUpDto();
        User user = createUnfinishedUser();
        OTP otp = createOTP(user.getId());
        ResponseOTPIdDto expected = new ResponseOTPIdDto(otp.getId());

        when(userRepository.existsByEmail(requestSignUpDto.getEmail())).thenReturn(false);
        when(userRepository.existsByPhoneNumber(requestSignUpDto.getPhoneNumber())).thenReturn(false);
        when(roleRepository.findByName("CUSTOMER")).thenReturn(Optional.of(createRole()));

        when(userMapper.requestSignUpDtoToUser(requestSignUpDto)).thenReturn(user);
        when(passwordEncoder.encode(requestSignUpDto.getPassword())).thenReturn(requestSignUpDto.getPassword());
        when(otpUtils.createOTP(user.getId(), Intent.signUp)).thenReturn(otp);

        //Act
        ResponseOTPIdDto result = signUpService.registerUser(requestSignUpDto);

        //Verify
        assertEquals(expected, result);
        verify(userRepository).save(eq(user));
        verify(otpRepository).set(eq(otp), eq(Duration.of(5, ChronoUnit.MINUTES)));
        verify(emailNotificationSender).sendToEmailOTPTopic(eq(new KafkaOTPDto(user.getFirstname(), user.getEmail(), otp.getValue())));
    }

    @Test
    @DisplayName("Должен выбросывать ошибку если почта занята")
    void registerUserShouldThrowUserAlreadyExistsException1(){

        //Arrange
        RequestSignUpDto requestSignUpDto = createRequestSignUpDto();

        when(userRepository.existsByEmail(requestSignUpDto.getEmail())).thenReturn(true);

        //Act
        UserAlreadyExistsException actual = assertThrows(
                UserAlreadyExistsException.class,
                () -> signUpService.registerUser(requestSignUpDto));

        //Verify
        assertEquals("User with this email already exists.", actual.getMessage());
    }

    @Test
    @DisplayName("Должен выбросывать ошибку если телефон занят")
    void registerUserShouldThrowUserAlreadyExistsException2(){

        //Arrange
        RequestSignUpDto requestSignUpDto = createRequestSignUpDto();

        when(userRepository.existsByEmail(requestSignUpDto.getEmail())).thenReturn(false);
        when(userRepository.existsByPhoneNumber(requestSignUpDto.getPhoneNumber())).thenReturn(true);

        //Act
        UserAlreadyExistsException actual = assertThrows(
                UserAlreadyExistsException.class,
                () -> signUpService.registerUser(requestSignUpDto));

        //Verify
        assertEquals("User with this phone number already exists.", actual.getMessage());
    }

    @Test
    @DisplayName("Должен выбросывать ошибку если не найдена роль по умолчанию")
    void registerUserShouldThrowInternalServerLogicException(){

        //Arrange
        RequestSignUpDto requestSignUpDto = createRequestSignUpDto();

        when(userRepository.existsByEmail(requestSignUpDto.getEmail())).thenReturn(false);
        when(userRepository.existsByPhoneNumber(requestSignUpDto.getPhoneNumber())).thenReturn(false);
        when(roleRepository.findByName("CUSTOMER")).thenReturn(Optional.empty());

        //Act
        InternalServerLogicException actual = assertThrows(
                InternalServerLogicException.class,
                () -> signUpService.registerUser(requestSignUpDto));

        //Verify
        assertEquals("No default role found", actual.getMessage());
    }

    @Test
    @DisplayName("Должен успешно возвращать дто и уведомлять пользователя")
    void resendOTPWithEmailShouldRegisterSuccessfully(){

        //Arrange
        User user = createPendingUser();
        RequestEmailDto requestEmailDto = new RequestEmailDto();
        requestEmailDto.setEmail(user.getEmail());
        OTP otp = createOTP(user.getId());
        ResponseOTPIdDto expected = new ResponseOTPIdDto(otp.getId());

        when(userRepository.findUserByEmail(requestEmailDto.getEmail())).thenReturn(Optional.of(user));
        when(otpUtils.createOTP(user.getId(), Intent.signUp)).thenReturn(otp);

        //Act
        ResponseOTPIdDto result = signUpService.resendOTPWithEmail(requestEmailDto);

        //Verify
        assertEquals(expected, result);
        verify(otpRepository).set(eq(otp), eq(Duration.of(5, ChronoUnit.MINUTES)));
        verify(emailNotificationSender).sendToEmailOTPTopic(eq(new KafkaOTPDto(user.getFirstname(), user.getEmail(), otp.getValue())));
    }

    @Test
    @DisplayName("Должен выбрасывать ошибку если пользователь не зарегистрирован")
    void resendOTPWithEmailShouldThrowUserNotFoundException(){

        //Arrange
        User user = createPendingUser();
        RequestEmailDto requestEmailDto = new RequestEmailDto();
        requestEmailDto.setEmail(user.getEmail());

        when(userRepository.findUserByEmail(requestEmailDto.getEmail())).thenReturn(Optional.empty());

        //Act
        UserNotFoundException actual = assertThrows(
                UserNotFoundException.class,
                () -> signUpService.resendOTPWithEmail(requestEmailDto));

        //Verify
        assertEquals(String.format("User with email %s not found", requestEmailDto.getEmail()), actual.getMessage());
    }

    @Test
    @DisplayName("Должен выбрасывать ошибку если статус не совпадает")
    void resendOTPWithEmailShouldThrowRequestDeniedException(){

        //Arrange
        User user = createPendingUser();
        RequestEmailDto requestEmailDto = new RequestEmailDto();
        requestEmailDto.setEmail(user.getEmail());
        user.setStatus(UserStatus.active);

        when(userRepository.findUserByEmail(requestEmailDto.getEmail())).thenReturn(Optional.of(user));

        //Act
        RequestDeniedException actual = assertThrows(
                RequestDeniedException.class,
                () -> signUpService.resendOTPWithEmail(requestEmailDto));

        //Verify
        assertEquals(String.format("User status is %s instead of %s", user.getStatus(), UserStatus.pending), actual.getMessage());
    }

    @Test
    @DisplayName("Должен успешно изменять статус пользователя")
    void confirmRegistrationShouldRegisterSuccessfully(){

        //Arrange
        User user = createPendingUser();
        OTP otp = createOTP(user.getId());

        RequestOTPDto requestOTPDto = new RequestOTPDto();
        requestOTPDto.setId(UUID.randomUUID());
        requestOTPDto.setOTP(otp.getValue());

        when(otpRepository.getByIntentAndId(Intent.signUp, requestOTPDto.getId())).thenReturn(Optional.of(otp));
        when(userRepository.findById(otp.getUserId())).thenReturn(Optional.of(user));

        //Act
        signUpService.confirmRegistration(requestOTPDto);

        //Verify
        assertEquals(UserStatus.active, user.getStatus());
        verify(userRepository).save(user);
        verify(otpRepository).deleteByIdAndIntent(otp.getId(), otp.getIntent());
    }

    @Test
    @DisplayName("Должен выбрасывать ошибку если код подтверждения не найден")
    void confirmRegistrationShouldThrowOTPTimedOutException(){

        //Arrange
        User user = createPendingUser();
        OTP otp = createOTP(user.getId());

        RequestOTPDto requestOTPDto = new RequestOTPDto();
        requestOTPDto.setId(UUID.randomUUID());
        requestOTPDto.setOTP(otp.getValue());

        when(otpRepository.getByIntentAndId(Intent.signUp, requestOTPDto.getId())).thenReturn(Optional.empty());

        //Act
        OTPTimedOutException actual = assertThrows(
                OTPTimedOutException.class,
                () -> signUpService.confirmRegistration(requestOTPDto));

        //Verify
        assertEquals("Confirmation code timed out or don't exist", actual.getMessage());
    }

    @Test
    @DisplayName("Должен выбрасывать ошибку если пользователь не существует")
    void confirmRegistrationShouldThrowUserNotFoundException(){

        //Arrange
        User user = createPendingUser();
        OTP otp = createOTP(user.getId());

        RequestOTPDto requestOTPDto = new RequestOTPDto();
        requestOTPDto.setId(UUID.randomUUID());
        requestOTPDto.setOTP(otp.getValue());

        when(otpRepository.getByIntentAndId(Intent.signUp, requestOTPDto.getId())).thenReturn(Optional.of(otp));
        when(userRepository.findById(otp.getUserId())).thenReturn(Optional.empty());

        //Act
        UserNotFoundException actual = assertThrows(
                UserNotFoundException.class,
                () -> signUpService.confirmRegistration(requestOTPDto));

        //Verify
        assertEquals("Linked user not found", actual.getMessage());
    }

    @Test
    @DisplayName("Должен выбрасывать ошибку если статутс не совпадает")
    void confirmRegistrationShouldThrowRequestDeniedException(){

        //Arrange
        User user = createPendingUser();
        user.setStatus(UserStatus.active);
        OTP otp = createOTP(user.getId());

        RequestOTPDto requestOTPDto = new RequestOTPDto();
        requestOTPDto.setId(UUID.randomUUID());
        requestOTPDto.setOTP(otp.getValue());

        when(otpRepository.getByIntentAndId(Intent.signUp, requestOTPDto.getId())).thenReturn(Optional.of(otp));
        when(userRepository.findById(otp.getUserId())).thenReturn(Optional.of(user));

        //Act
        RequestDeniedException actual = assertThrows(
                RequestDeniedException.class,
                () -> signUpService.confirmRegistration(requestOTPDto));

        //Verify
        assertEquals(String.format("User status is %s instead of %s", user.getStatus(), UserStatus.pending), actual.getMessage());
    }

    @Test
    @DisplayName("Должен выбрасывать ошибку если введён неверный код")
    void confirmRegistrationShouldThrowOTPException(){

        //Arrange
        User user = createPendingUser();
        OTP otp = createOTP(user.getId());

        RequestOTPDto requestOTPDto = new RequestOTPDto();
        requestOTPDto.setId(UUID.randomUUID());
        requestOTPDto.setOTP("9999");

        when(otpRepository.getByIntentAndId(Intent.signUp, requestOTPDto.getId())).thenReturn(Optional.of(otp));
        when(userRepository.findById(otp.getUserId())).thenReturn(Optional.of(user));

        //Act
        OTPException actual = assertThrows(
                OTPException.class,
                () -> signUpService.confirmRegistration(requestOTPDto));

        //Verify
        assertEquals("Invalid 4-digits code", actual.getMessage());
    }

    private User createUnfinishedUser(){
        return User.builder()
                .id(UUID.randomUUID())
                .email("example@example.com")
                .password("1234")
                .firstname("Иванович")
                .lastname("Иванов")
                .patronymic("Иванович")
                .phoneNumber("123456789012")
                .birthDate(LocalDate.parse("1990-01-01"))
                .build();
    }

    private User createPendingUser(){
        User user = createUnfinishedUser();
        user.setRegistrationDate(OffsetDateTime.now());
        user.setAccessDate(OffsetDateTime.now());
        user.setStatus(UserStatus.pending);
        user.setRole(createRole());
        return user;
    }

    private Role createRole(){
        return Role.builder()
                .id(UUID.randomUUID())
                .name("CUSTOMER")
                .description("Role desc")
                .build();
    }

    private RequestSignUpDto createRequestSignUpDto(){
        return RequestSignUpDto.builder()
                .email("example@example.com")
                .password("1234")
                .firstname("Иван")
                .lastname("Иванов")
                .patronymic("Иванович")
                .phoneNumber("123456789012")
                .birthDate(LocalDate.parse("1990-01-01"))
                .build();
    }

    private OTP createOTP(UUID userId){
        return OTP.builder()
                .id(UUID.randomUUID())
                .userId(userId)
                .intent(Intent.signUp)
                .value("0000")
                .build();
    }

}
