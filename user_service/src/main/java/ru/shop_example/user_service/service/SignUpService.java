package ru.shop_example.user_service.service;

import ru.shop_example.user_service.dto.ConfirmationCodeIdResponseDto;
import ru.shop_example.user_service.dto.EmailDto;
import ru.shop_example.user_service.dto.RequestSignUpDto;
import ru.shop_example.user_service.dto.ConfirmationCodeDto;

public interface SignUpService {
    ConfirmationCodeIdResponseDto registerUser(RequestSignUpDto requestSignUpDto);
    ConfirmationCodeIdResponseDto resendConfirmationCodeWithEmail(EmailDto emailDto);
    void confirmRegistration(ConfirmationCodeDto confirmationCodeDto);
}
