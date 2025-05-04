package ru.shop_example.user_service.service.implementation;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.shop_example.user_service.dto.PasswordDto;
import ru.shop_example.user_service.dto.UpdateUserProfileDto;
import ru.shop_example.user_service.dto.UserInfoDto;
import ru.shop_example.user_service.entity.User;
import ru.shop_example.user_service.entity.constant.UserStatus;
import ru.shop_example.user_service.exception.custom.AuthorizationFailedException;
import ru.shop_example.user_service.exception.custom.RequestDeniedException;
import ru.shop_example.user_service.exception.custom.UserNotFoundException;
import ru.shop_example.user_service.mapper.UserMapper;
import ru.shop_example.user_service.repository.UserRepository;
import ru.shop_example.user_service.service.UserService;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final UserMapper userMapper;

    public UserInfoDto getProfile(UUID userId){
        return userMapper.userToUserInfoDto(userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException("User not found")));
    }

    @Transactional
    public void updateProfile(UUID userID, UpdateUserProfileDto updateUserProfileDto){
        log.info("Called updateProfile service method");
        User user = userRepository.findById(userID).orElseThrow(() -> new UserNotFoundException("User not found"));
        if (!user.getStatus().equals(UserStatus.active)) throw new RequestDeniedException(String.format("User status is %s instead of %s", user.getStatus(), UserStatus.active));
        if (!passwordEncoder.matches(updateUserProfileDto.getOldPassword(), user.getPassword())) throw new AuthorizationFailedException("Wrong password");
        user.setEmail(updateUserProfileDto.getEmail());
        user.setPassword(passwordEncoder.encode(updateUserProfileDto.getPassword()));
        user.setFirstname(updateUserProfileDto.getFirstname());
        user.setLastname(updateUserProfileDto.getLastname());
        user.setPatronymic(updateUserProfileDto.getPatronymic());
        user.setBirthDate(updateUserProfileDto.getBirthDate());
        user.setPhoneNumber(updateUserProfileDto.getPhoneNumber());
    }

    @Transactional
    public void deleteProfile(UUID userID, PasswordDto passwordDto){
        log.info("Called deleteProfile service method");
        User user = userRepository.findById(userID).orElseThrow(() -> new UserNotFoundException("User not found"));
        if (!user.getStatus().equals(UserStatus.active)) throw new RequestDeniedException(String.format("User status is %s instead of %s", user.getStatus(), UserStatus.active));
        if (!passwordEncoder.matches(passwordDto.getPassword(), user.getPassword())) throw new AuthorizationFailedException("Wrong password");
        user.setStatus(UserStatus.markedForDeletion);
    }
}
