package ru.shop_example.user_service.service;

import ru.shop_example.user_service.dto.RequestRefreshTokenDto;
import ru.shop_example.user_service.dto.RequestSignInDto;
import ru.shop_example.user_service.dto.ResponseSignInDto;

public interface SignInService {

    ResponseSignInDto signIn(RequestSignInDto requestSignInDto);
    ResponseSignInDto signInWithRefreshToken(RequestRefreshTokenDto requestRefreshTokenDto);
}
