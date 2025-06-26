package ru.shop_example.user_service.mapper;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import ru.shop_example.user_service.dto.RequestSignUpDto;
import ru.shop_example.user_service.dto.ResponseUserInfoDto;
import ru.shop_example.user_service.entity.Role;
import ru.shop_example.user_service.entity.User;
import ru.shop_example.user_service.entity.constant.UserStatus;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class UserMapperTest {

    UserMapper mapper = Mappers.getMapper(UserMapper.class);

    @Test
    @DisplayName("Должен возвращать корректную сущность")
    void requestSignUpDtoToUserShouldReturnUser(){

        //Arrange
        User user = createActiveUser();
        ResponseUserInfoDto expected = createResponseUserInfoDto(user);

        //Act
        ResponseUserInfoDto actual = mapper.userToUserInfoDto(user);

        //Verify
        assertEquals(expected, actual);
    }

    @Test
    @DisplayName("Должен возвращать корректное дто")
    void userToUserInfoDtoShouldReturnDto(){

        //Arrange
        User expected = createActiveUser();
        RequestSignUpDto request = createRequestSignUpDto(expected);

        //Act
        User actual = mapper.requestSignUpDtoToUser(request);

        //Verify
        assertThat(actual)
                .extracting(
                        User::getEmail,
                        User::getPassword,
                        User::getFirstname,
                        User::getLastname,
                        User::getPatronymic,
                        User::getPhoneNumber,
                        User::getBirthDate
                )
                .containsExactly(
                        expected.getEmail(),
                        expected.getPassword(),
                        expected.getFirstname(),
                        expected.getLastname(),
                        expected.getPatronymic(),
                        expected.getPhoneNumber(),
                        expected.getBirthDate()
                );
    }

    private User createActiveUser(){
        return User.builder()
                .id(UUID.randomUUID())
                .email("example@example.com")
                .password("1234")
                .firstname("Иванович")
                .lastname("Иванов")
                .patronymic("Иванович")
                .phoneNumber("123456789012")
                .birthDate(LocalDate.parse("1990-01-01"))
                .accessDate(OffsetDateTime.now())
                .registrationDate(OffsetDateTime.now())
                .status(UserStatus.active)
                .role(createRole())
                .build();
    }

    private Role createRole(){
        return Role.builder()
                .id(UUID.randomUUID())
                .name("CUSTOMER")
                .description("Role desc")
                .build();
    }

    private ResponseUserInfoDto createResponseUserInfoDto(User user){
        return ResponseUserInfoDto.builder()
                .id(user.getId())
                .email(user.getEmail())
                .firstname(user.getFirstname())
                .lastname(user.getLastname())
                .patronymic(user.getPatronymic())
                .phoneNumber(user.getPhoneNumber())
                .birthDate(user.getBirthDate())
                .accessDate(user.getAccessDate())
                .registrationDate(user.getRegistrationDate())
                .status(user.getStatus())
                .roleId(user.getRole().getId())
                .build();
    }

    private RequestSignUpDto createRequestSignUpDto(User user){
        return RequestSignUpDto.builder()
                .email(user.getEmail())
                .password(user.getPassword())
                .firstname(user.getFirstname())
                .lastname(user.getLastname())
                .patronymic(user.getPatronymic())
                .phoneNumber(user.getPhoneNumber())
                .birthDate(user.getBirthDate())
                .build();
    }
}
