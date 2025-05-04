package ru.shop_example.user_service.service;

import ru.shop_example.user_service.dto.PasswordDto;
import ru.shop_example.user_service.dto.UpdateUserProfileDto;
import ru.shop_example.user_service.dto.UserInfoDto;

import java.util.UUID;

public interface UserService {
    UserInfoDto getProfile(UUID userId);
    void updateProfile(UUID userID, UpdateUserProfileDto updateUserProfileDto);
    void deleteProfile(UUID userID, PasswordDto passwordDto);
}
