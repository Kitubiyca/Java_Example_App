package ru.shop_example.user_service.util;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.shop_example.user_service.entity.OTP;
import ru.shop_example.user_service.entity.constant.Intent;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(MockitoExtension.class)
public class OTPUtilsTest {

    @Spy
    private OTPUtils otpUtils;

    @Test
    @DisplayName("Должен возвращать корректный код подтверждения")
    void createOTPShouldReturnOTP(){

        //Arrange
        UUID userId = UUID.randomUUID();
        Intent intent = Intent.signUp;

        //Act
        OTP actual = otpUtils.createOTP(userId, intent);

        //Verify
        assertEquals(userId, actual.getUserId());
        assertEquals(intent, actual.getIntent());
        assertEquals(4, actual.getValue().length());

        int value = Integer.parseInt(actual.getValue());
        assertTrue(value < 10000);
        assertTrue(value >= 0);
    }

}
