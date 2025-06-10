package ru.shop_example.user_service.service;

import ru.shop_example.user_service.dto.OTPIdResponseDto;
import ru.shop_example.user_service.dto.EmailDto;
import ru.shop_example.user_service.dto.RequestSignUpDto;
import ru.shop_example.user_service.dto.OTPDto;

public interface SignUpService {
    OTPIdResponseDto registerUser(RequestSignUpDto requestSignUpDto);
    OTPIdResponseDto resendOTPWithEmail(EmailDto emailDto);
    void confirmRegistration(OTPDto OTPDto);
}
