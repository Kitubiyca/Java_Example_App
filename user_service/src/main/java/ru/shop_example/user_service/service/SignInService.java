package ru.shop_example.user_service.service;

import ru.shop_example.user_service.dto.RefreshTokenDto;
import ru.shop_example.user_service.dto.SignInDto;
import ru.shop_example.user_service.dto.SignInResponseDto;

public interface SignInService {

    SignInResponseDto signIn(SignInDto signInDto);
    SignInResponseDto signInWithRefreshToken(RefreshTokenDto refreshTokenDto);
}
