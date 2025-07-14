package ru.shop_example.user_service.repository;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.redis.DataRedisTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import ru.shop_example.user_service.common.containers.RedisContainer;
import ru.shop_example.user_service.configuration.RedisTemplateConfiguration;
import ru.shop_example.user_service.entity.OTP;
import ru.shop_example.user_service.entity.constant.Intent;
import ru.shop_example.user_service.repository.implementation.OTPRepositoryImpl;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@DataRedisTest
@ActiveProfiles("test")
@Import({OTPRepositoryImpl.class, RedisTemplateConfiguration.class})
public class OTPRepositoryIT implements RedisContainer {

    @Autowired
    private OTPRepository otpRepository;

    @Test
    @DisplayName("Должен сохранять и читать код")
    void shouldSaveAndReadOTP(){

        //Arrange
        OTP otp = OTP.builder()
                .id(UUID.randomUUID())
                .userId(UUID.randomUUID())
                .intent(Intent.signUp)
                .value("0000")
                .build();

        //Act
        otpRepository.set(otp, Duration.of(5, ChronoUnit.MINUTES));
        Optional<OTP> result = otpRepository.getByIntentAndId(otp.getIntent(), otp.getId());

        //Verify
        assertThat(result).isNotEmpty();
        assertThat(result.get().getId()).isEqualTo(otp.getId());
        assertThat(result.get().getUserId()).isEqualTo(otp.getUserId());
        assertThat(result.get().getIntent()).isEqualTo(otp.getIntent());
        assertThat(result.get().getValue()).isEqualTo(otp.getValue());
    }

    @Test
    @DisplayName("Должен сохранять и корретно читать несущестующий код")
    void shouldSaveAndReadNonExistentOTP(){

        //Arrange
        OTP otp = OTP.builder()
                .id(UUID.randomUUID())
                .userId(UUID.randomUUID())
                .intent(Intent.signUp)
                .value("0000")
                .build();

        //Act
        otpRepository.set(otp, Duration.of(5, ChronoUnit.MINUTES));
        Optional<OTP> result = otpRepository.getByIntentAndId(otp.getIntent(), UUID.randomUUID());

        //Verify
        assertThat(result).isEmpty();
    }

    @Test
    @DisplayName("Должен корректно удалять код")
    void shouldSaveAndDeleteAfterwardsOTP(){

        //Arrange
        OTP otp = OTP.builder()
                .id(UUID.randomUUID())
                .userId(UUID.randomUUID())
                .intent(Intent.signUp)
                .value("0000")
                .build();

        //Act
        otpRepository.set(otp, Duration.of(5, ChronoUnit.MINUTES));
        Optional<OTP> result1 = otpRepository.getByIntentAndId(otp.getIntent(), otp.getId());

        otpRepository.deleteByIdAndIntent(otp.getId(), otp.getIntent());
        Optional<OTP> result2 = otpRepository.getByIntentAndId(otp.getIntent(), otp.getId());

        //Verify
        assertThat(result1).isNotEmpty();
        assertThat(result2).isEmpty();
    }
}
