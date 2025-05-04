package ru.shop_example.user_service.service.implementation;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.shop_example.user_service.dto.ConfirmationCodeIdResponseDto;
import ru.shop_example.user_service.dto.EmailDto;
import ru.shop_example.user_service.dto.RequestSignUpDto;
import ru.shop_example.user_service.dto.ConfirmationCodeDto;
import ru.shop_example.user_service.dto.redis.RedisConfirmationCodeDto;
import ru.shop_example.user_service.entity.Role;
import ru.shop_example.user_service.entity.User;
import ru.shop_example.user_service.entity.constant.Intent;
import ru.shop_example.user_service.entity.constant.UserStatus;
import ru.shop_example.user_service.exception.custom.*;
import ru.shop_example.user_service.mapper.UserMapper;
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
    private final RedisTemplate<String, RedisConfirmationCodeDto> redisConfirmationCodeDtoRedisTemplate;
    private final BCryptPasswordEncoder passwordEncoder;
    private final UserMapper userMapper;
    private final Utils utils;

    @Transactional
    public ConfirmationCodeIdResponseDto registerUser(RequestSignUpDto requestSignUpDto){
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
        return new ConfirmationCodeIdResponseDto(utils.sendConfirmationCodeWithEmail(user, Intent.signUp, Duration.of(5, ChronoUnit.MINUTES)));
    }

    public ConfirmationCodeIdResponseDto resendConfirmationCodeWithEmail(EmailDto emailDto){
        log.info("Called resendConfirmationCodeWithEmail service method");
        User user = userRepository.findUserByEmail(emailDto.getEmail()).orElseThrow(() -> new UserNotFoundException(String.format("User with email %s not found", emailDto.getEmail())));
        if (!user.getStatus().equals(UserStatus.pending)) throw new RequestDeniedException(String.format("User status is %s instead of %s", user.getStatus(), UserStatus.pending));
        return new ConfirmationCodeIdResponseDto(utils.sendConfirmationCodeWithEmail(user, Intent.signUp, Duration.of(5, ChronoUnit.MINUTES)));
    }

    @Transactional
    public void confirmRegistration(ConfirmationCodeDto confirmationCodeDto){
        log.info("Called ConfirmRegistration service method");
        RedisConfirmationCodeDto redisConfirmationCodeDto;
        redisConfirmationCodeDto = redisConfirmationCodeDtoRedisTemplate.opsForValue().get(String.format("code:%s:%s", Intent.signUp, confirmationCodeDto.getId()));
        if (redisConfirmationCodeDto == null) throw new ConfirmationCodeTimedOutException("Confirmation code timed out or don't exist");
        User user = userRepository.findById(redisConfirmationCodeDto.getUserId()).orElseThrow(() -> new UserNotFoundException("Linked user not found"));
        if (!user.getStatus().equals(UserStatus.pending)) throw new RequestDeniedException(String.format("User status is %s instead of %s", user.getStatus(), UserStatus.pending));
        if (!redisConfirmationCodeDto.getValue().equals(confirmationCodeDto.getValue())) throw new OTPException("Invalid 4-digits code");
        user.setStatus(UserStatus.active);
        userRepository.save(user);
        redisConfirmationCodeDtoRedisTemplate.delete(String.format("code:%s:%s", Intent.signUp, confirmationCodeDto.getId()));
    }
}
