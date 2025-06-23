package ru.shop_example.user_service.service;

import ru.shop_example.user_service.dto.RequestPasswordDto;
import ru.shop_example.user_service.dto.RequestUpdateUserProfileDto;
import ru.shop_example.user_service.dto.ResponseUserInfoDto;

import java.util.UUID;

public interface UserService {
    ResponseUserInfoDto getProfile(UUID userId);
    void updateProfile(UUID userID, RequestUpdateUserProfileDto requestUpdateUserProfileDto);
    void deleteProfile(UUID userID, RequestPasswordDto requestPasswordDto);
}
