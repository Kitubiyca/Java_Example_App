package ru.shop_example.user_service.service.implementation;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.shop_example.user_service.dto.RequestPasswordDto;
import ru.shop_example.user_service.dto.RequestUpdateUserProfileDto;
import ru.shop_example.user_service.dto.ResponseUserInfoDto;
import ru.shop_example.user_service.entity.User;
import ru.shop_example.user_service.entity.constant.UserStatus;
import ru.shop_example.user_service.exception.custom.AuthorizationFailedException;
import ru.shop_example.user_service.exception.custom.RequestDeniedException;
import ru.shop_example.user_service.exception.custom.UserNotFoundException;
import ru.shop_example.user_service.mapper.UserMapper;
import ru.shop_example.user_service.repository.UserRepository;
import ru.shop_example.user_service.service.UserService;
import ru.shop_example.user_service.controller.UserController;

import java.util.UUID;

/**
 * Реализация сервиса для работы с доменной сущностью пользователя.
 *
 * @see User
 * @see UserController
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final UserMapper userMapper;

    /**
     * {@inheritDoc}
     */
    public ResponseUserInfoDto getProfile(UUID userId){
        return userMapper.userToUserInfoDto(userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException("User not found")));
    }

    /**
     * {@inheritDoc}
     */
    @Transactional
    public void updateProfile(UUID userID, RequestUpdateUserProfileDto requestUpdateUserProfileDto){
        log.info("Called updateProfile service method");
        User user = userRepository.findById(userID).orElseThrow(() -> new UserNotFoundException("User not found"));
        if (!user.getStatus().equals(UserStatus.active)) throw new RequestDeniedException(String.format("User status is %s instead of %s", user.getStatus(), UserStatus.active));
        if (!passwordEncoder.matches(requestUpdateUserProfileDto.getOldPassword(), user.getPassword())) throw new AuthorizationFailedException("Wrong password");
        user.setEmail(requestUpdateUserProfileDto.getEmail());
        user.setPassword(passwordEncoder.encode(requestUpdateUserProfileDto.getPassword()));
        user.setFirstname(requestUpdateUserProfileDto.getFirstname());
        user.setLastname(requestUpdateUserProfileDto.getLastname());
        user.setPatronymic(requestUpdateUserProfileDto.getPatronymic());
        user.setBirthDate(requestUpdateUserProfileDto.getBirthDate());
        user.setPhoneNumber(requestUpdateUserProfileDto.getPhoneNumber());
    }

    /**
     * {@inheritDoc}
     */
    @Transactional
    public void deleteProfile(UUID userID, RequestPasswordDto requestPasswordDto){
        log.info("Called deleteProfile service method");
        User user = userRepository.findById(userID).orElseThrow(() -> new UserNotFoundException("User not found"));
        if (!user.getStatus().equals(UserStatus.active)) throw new RequestDeniedException(String.format("User status is %s instead of %s", user.getStatus(), UserStatus.active));
        if (!passwordEncoder.matches(requestPasswordDto.getPassword(), user.getPassword())) throw new AuthorizationFailedException("Wrong password");
        user.setStatus(UserStatus.markedForDeletion);
    }
}
