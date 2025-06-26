package ru.shop_example.user_service.service.implementation;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import ru.shop_example.user_service.dto.RequestPasswordDto;
import ru.shop_example.user_service.dto.RequestUpdateUserProfileDto;
import ru.shop_example.user_service.dto.ResponseUserInfoDto;
import ru.shop_example.user_service.entity.Role;
import ru.shop_example.user_service.entity.User;
import ru.shop_example.user_service.entity.constant.UserStatus;
import ru.shop_example.user_service.exception.custom.AuthorizationFailedException;
import ru.shop_example.user_service.exception.custom.RequestDeniedException;
import ru.shop_example.user_service.exception.custom.UserNotFoundException;
import ru.shop_example.user_service.mapper.UserMapper;
import ru.shop_example.user_service.repository.UserRepository;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private BCryptPasswordEncoder passwordEncoder;
    @Mock
    private UserMapper userMapper;
    @Spy
    @InjectMocks
    private UserServiceImpl userService;

    @Test
    @DisplayName("Должен возвращать корректное дто с информацией")
    void getProfileShouldReturnDto(){

        //Arrange
        User user = createActiveUser1();
        ResponseUserInfoDto expected = createResponseUserInfoDto(user);

        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        when(userMapper.userToUserInfoDto(user)).thenReturn(expected);

        //Act
        ResponseUserInfoDto actual = userService.getProfile(user.getId());

        //Verify
        assertEquals(expected, actual);
    }

    @Test
    @DisplayName("Должен выбрасывать ошибку если пользователь не найден")
    void getProfileShouldThrowUserNotFoundException(){

        //Arrange
        UUID userId = UUID.randomUUID();

        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        //Act
        UserNotFoundException actual = assertThrows(
                UserNotFoundException.class,
                () -> userService.getProfile(userId));

        //Verify
        assertEquals("User not found", actual.getMessage());
    }

    @Test
    @DisplayName("Должен обновлять пользователя согласно данным из дто")
    void updateProfileShouldUpdateEntity(){

        //Arrange
        User user = createActiveUser1();
        RequestUpdateUserProfileDto request = createRequestUpdateUserProfileDto(createActiveUser2(), user.getPassword());

        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(request.getOldPassword(), user.getPassword())).thenReturn(true);
        when((passwordEncoder.encode(request.getPassword()))).thenReturn(request.getPassword());

        //Act
        userService.updateProfile(user.getId(), request);

        //Verify
        assertThat(user)
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
                        request.getEmail(),
                        request.getPassword(),
                        request.getFirstname(),
                        request.getLastname(),
                        request.getPatronymic(),
                        request.getPhoneNumber(),
                        request.getBirthDate()
                );
    }

    @Test
    @DisplayName("Должен выбрасывать ошибку если пользователь не найден")
    void updateProfileShouldThrowUserNotFoundException(){

        //Arrange
        User user = createActiveUser1();
        RequestUpdateUserProfileDto request = createRequestUpdateUserProfileDto(createActiveUser2(), user.getPassword());

        when(userRepository.findById(user.getId())).thenReturn(Optional.empty());

        //Act
        UserNotFoundException actual = assertThrows(
                UserNotFoundException.class,
                () -> userService.updateProfile(user.getId(), request));

        //Verify
        assertEquals("User not found", actual.getMessage());
    }

    @Test
    @DisplayName("Должен выбрасывать ошибку если статус не совпадает")
    void updateProfileShouldThrowRequestDeniedException(){

        //Arrange
        User user = createActiveUser1();
        user.setStatus(UserStatus.suspended);
        RequestUpdateUserProfileDto request = createRequestUpdateUserProfileDto(createActiveUser2(), user.getPassword());

        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));

        //Act
        RequestDeniedException actual = assertThrows(
                RequestDeniedException.class,
                () -> userService.updateProfile(user.getId(), request));

        //Verify
        assertEquals(String.format("User status is %s instead of %s", user.getStatus(), UserStatus.active), actual.getMessage());
    }

    @Test
    @DisplayName("Должен выбрасывать ошибку если пароль не совпадает")
    void updateProfileShouldThrowAuthorizationFailedException(){

        //Arrange
        User user = createActiveUser1();
        RequestUpdateUserProfileDto request = createRequestUpdateUserProfileDto(createActiveUser2(), user.getPassword());

        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(request.getOldPassword(), user.getPassword())).thenReturn(false);

        //Act
        AuthorizationFailedException actual = assertThrows(
                AuthorizationFailedException.class,
                () -> userService.updateProfile(user.getId(), request));

        //Verify
        assertEquals("Wrong password", actual.getMessage());
    }

    @Test
    @DisplayName("Должен менять статус пользователя")
    void deleteProfileShouldChangeStatus(){

        //Arrange
        User user = createActiveUser1();
        RequestPasswordDto requestPasswordDto = new RequestPasswordDto(user.getPassword());

        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(requestPasswordDto.getPassword(), user.getPassword())).thenReturn(true);

        //Act
        userService.deleteProfile(user.getId(), requestPasswordDto);

        //Verify
        assertEquals(UserStatus.markedForDeletion, user.getStatus());
    }

    @Test
    @DisplayName("Должен выбрасывать ошибку если пользователь не найден")
    void deleteProfileShouldThrowUserNotFoundException(){

        //Arrange
        User user = createActiveUser1();
        RequestPasswordDto requestPasswordDto = new RequestPasswordDto(user.getPassword());

        when(userRepository.findById(user.getId())).thenReturn(Optional.empty());

        //Act
        UserNotFoundException actual = assertThrows(
                UserNotFoundException.class,
                () -> userService.deleteProfile(user.getId(), requestPasswordDto));

        //Verify
        assertEquals("User not found", actual.getMessage());
    }

    @Test
    @DisplayName("Должен выбрасывать ошибку если статус не совпадает")
    void deleteProfileShouldThrowRequestDeniedException(){

        //Arrange
        User user = createActiveUser1();
        user.setStatus(UserStatus.suspended);
        RequestPasswordDto requestPasswordDto = new RequestPasswordDto(user.getPassword());

        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));

        //Act
        RequestDeniedException actual = assertThrows(
                RequestDeniedException.class,
                () -> userService.deleteProfile(user.getId(), requestPasswordDto));

        //Verify
        assertEquals(String.format("User status is %s instead of %s", user.getStatus(), UserStatus.active), actual.getMessage());
    }

    @Test
    @DisplayName("Должен выбрасывать ошибку если пароль не совпадает")
    void deleteProfileShouldThrowAuthorizationFailedException(){

        //Arrange
        User user = createActiveUser1();
        RequestPasswordDto requestPasswordDto = new RequestPasswordDto(user.getPassword());

        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(requestPasswordDto.getPassword(), user.getPassword())).thenReturn(false);

        //Act
        AuthorizationFailedException actual = assertThrows(
                AuthorizationFailedException.class,
                () -> userService.deleteProfile(user.getId(), requestPasswordDto));

        //Verify
        assertEquals("Wrong password", actual.getMessage());
    }

    private User createActiveUser1(){
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

    private User createActiveUser2(){
        return User.builder()
                .id(UUID.randomUUID())
                .email("new_example@example.com")
                .password("4321")
                .firstname("Сергей")
                .lastname("Сергеев")
                .patronymic("Сергеевич")
                .phoneNumber("012123456789")
                .birthDate(LocalDate.parse("1995-01-01"))
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

    private RequestUpdateUserProfileDto createRequestUpdateUserProfileDto(User newUser, String oldPassword){
        return RequestUpdateUserProfileDto.builder()
                .email(newUser.getEmail())
                .oldPassword(oldPassword)
                .password(newUser.getPassword())
                .firstname(newUser.getFirstname())
                .lastname(newUser.getLastname())
                .patronymic(newUser.getPatronymic())
                .phoneNumber(newUser.getPhoneNumber())
                .birthDate(newUser.getBirthDate())
                .build();
    }
}
