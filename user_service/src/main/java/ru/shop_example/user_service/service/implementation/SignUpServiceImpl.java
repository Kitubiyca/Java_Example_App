package ru.shop_example.user_service.service.implementation;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.shop_example.user_service.dto.OTPIdResponseDto;
import ru.shop_example.user_service.dto.EmailDto;
import ru.shop_example.user_service.dto.RequestSignUpDto;
import ru.shop_example.user_service.dto.OTPDto;
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
import ru.shop_example.user_service.util.Utils;

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
    private final UserMapper userMapper;
    private final Utils utils;

    @Transactional
    public OTPIdResponseDto registerUser(RequestSignUpDto requestSignUpDto){
        log.info("Called registerUser service method");
        if (userRepository.existsByEmail(requestSignUpDto.getEmail())) throw new UserAlreadyExistsException("User with this email already exists.");
        if (userRepository.existsByPhoneNumber(requestSignUpDto.getPhoneNumber())) throw new UserAlreadyExistsException("User with this phone number already exists.");
        Role userRole = roleRepository.findByName("CUSTOMER").orElseThrow(() -> new InternalServerLogicException("No default role found"));
        User user = userMapper.requestSignUpDto(requestSignUpDto);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRegistrationDate(OffsetDateTime.now());
        user.setAccessDate(user.getRegistrationDate());
        user.setStatus(UserStatus.pending);
        user.setRole(userRole);
        userRepository.save(user);
        return new OTPIdResponseDto(handleOTP(user).getId());
    }

    public OTPIdResponseDto resendOTPWithEmail(EmailDto emailDto){
        log.info("Called resendOTPWithEmail service method");
        User user = userRepository.findUserByEmail(emailDto.getEmail()).orElseThrow(() -> new UserNotFoundException(String.format("User with email %s not found", emailDto.getEmail())));
        if (!user.getStatus().equals(UserStatus.pending)) throw new RequestDeniedException(String.format("User status is %s instead of %s", user.getStatus(), UserStatus.pending));
        return new OTPIdResponseDto(handleOTP(user).getId());
    }

    @Transactional
    public void confirmRegistration(OTPDto OTPDto){
        log.info("Called ConfirmRegistration service method");
        OTP otp = otpRepository.getByIntentAndId(Intent.signUp, OTPDto.getId());
        if (otp == null) throw new OTPTimedOutException("Confirmation code timed out or don't exist");
        User user = userRepository.findById(otp.getUserId()).orElseThrow(() -> new UserNotFoundException("Linked user not found"));
        if (!user.getStatus().equals(UserStatus.pending)) throw new RequestDeniedException(String.format("User status is %s instead of %s", user.getStatus(), UserStatus.pending));
        if (!otp.getValue().equals(OTPDto.getValue())) throw new OTPException("Invalid 4-digits code");
        user.setStatus(UserStatus.active);
        userRepository.save(user);
        otpRepository.deleteByIdAndIntent(otp.getId(), otp.getIntent());
    }

    private OTP handleOTP(User user){
        OTP otp = utils.createOTP(user.getId(), Intent.signUp);
        otpRepository.set(otp, Duration.of(5, ChronoUnit.MINUTES));
        utils.sendOTPWithEmail(user, otp);
        return otp;
    }
}
