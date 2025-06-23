package ru.shop_example.user_service.service;

import ru.shop_example.user_service.dto.ResponseOTPIdDto;
import ru.shop_example.user_service.dto.RequestEmailDto;
import ru.shop_example.user_service.dto.RequestSignUpDto;
import ru.shop_example.user_service.dto.RequestOTPDto;

public interface SignUpService {
    ResponseOTPIdDto registerUser(RequestSignUpDto requestSignUpDto);
    ResponseOTPIdDto resendOTPWithEmail(RequestEmailDto requestEmailDto);
    void confirmRegistration(RequestOTPDto RequestOTPDto);
}
