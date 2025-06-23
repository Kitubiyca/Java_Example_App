package ru.shop_example.user_service.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.shop_example.user_service.dto.RequestSignUpDto;
import ru.shop_example.user_service.dto.ResponseUserInfoDto;
import ru.shop_example.user_service.entity.User;

@Mapper(componentModel = "spring")
public interface UserMapper {
    User requestSignUpDto(RequestSignUpDto requestSignUpDto);

    @Mapping(source = "role.id", target = "roleId")
    ResponseUserInfoDto userToUserInfoDto(User user);
}
