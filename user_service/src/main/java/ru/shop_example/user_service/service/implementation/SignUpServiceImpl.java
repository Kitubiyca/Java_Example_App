package ru.shop_example.user_service.service.implementation;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.shop_example.user_service.dto.ResponseOTPIdDto;
import ru.shop_example.user_service.dto.RequestEmailDto;
import ru.shop_example.user_service.dto.RequestSignUpDto;
import ru.shop_example.user_service.dto.RequestOTPDto;
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
import ru.shop_example.user_service.service.SignUpService;
import ru.shop_example.user_service.service.kafka.EmailNotificationSender;
import ru.shop_example.user_service.util.OTPUtils;

import java.time.Duration;
import java.time.OffsetDateTime;
import java.time.temporal.ChronoUnit;

@Service
@RequiredArgsConstructor
@Slf4j
public class SignUpServiceImpl implements SignUpService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final OTPRepository otpRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final EmailNotificationSender emailNotificationSender;
    private final UserMapper userMapper;
    private final OTPUtils OTPUtils;

    @Transactional
    public ResponseOTPIdDto registerUser(RequestSignUpDto requestSignUpDto){
        log.info("Called registerUser service method");
        if (userRepository.existsByEmail(requestSignUpDto.getEmail())) throw new UserAlreadyExistsException("User with this email already exists.");
        if (userRepository.existsByPhoneNumber(requestSignUpDto.getPhoneNumber())) throw new UserAlreadyExistsException("User with this phone number already exists.");
        Role userRole = roleRepository.findByName("CUSTOMER").orElseThrow(() -> new InternalServerLogicException("No default role found"));
        User user = userMapper.requestSignUpDtoToUser(requestSignUpDto);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRegistrationDate(OffsetDateTime.now());
        user.setAccessDate(user.getRegistrationDate());
        user.setStatus(UserStatus.pending);
        user.setRole(userRole);
        userRepository.save(user);
        return new ResponseOTPIdDto(handleOTP(user).getId());
    }

    public ResponseOTPIdDto resendOTPWithEmail(RequestEmailDto requestEmailDto){
        log.info("Called resendOTPWithEmail service method");
        User user = userRepository.findUserByEmail(requestEmailDto.getEmail()).orElseThrow(() -> new UserNotFoundException(String.format("User with email %s not found", requestEmailDto.getEmail())));
        if (!user.getStatus().equals(UserStatus.pending)) throw new RequestDeniedException(String.format("User status is %s instead of %s", user.getStatus(), UserStatus.pending));
        return new ResponseOTPIdDto(handleOTP(user).getId());
    }

    @Transactional
    public void confirmRegistration(RequestOTPDto RequestOTPDto){
        log.info("Called ConfirmRegistration service method");
        OTP otp = otpRepository.getByIntentAndId(Intent.signUp, RequestOTPDto.getId()).orElseThrow(() -> new OTPTimedOutException("Confirmation code timed out or don't exist"));
        User user = userRepository.findById(otp.getUserId()).orElseThrow(() -> new UserNotFoundException("Linked user not found"));
        if (!user.getStatus().equals(UserStatus.pending)) throw new RequestDeniedException(String.format("User status is %s instead of %s", user.getStatus(), UserStatus.pending));
        if (!otp.getValue().equals(RequestOTPDto.getValue())) throw new OTPException("Invalid 4-digits code");
        user.setStatus(UserStatus.active);
        userRepository.save(user);
        otpRepository.deleteByIdAndIntent(otp.getId(), otp.getIntent());
    }

    private OTP handleOTP(User user){
        OTP otp = OTPUtils.createOTP(user.getId(), Intent.signUp);
        otpRepository.set(otp, Duration.of(5, ChronoUnit.MINUTES));
        emailNotificationSender.sendToEmailOTPTopic(new KafkaOTPDto(user.getFirstname(), user.getEmail(), otp.getValue()));
        return otp;
    }
}
