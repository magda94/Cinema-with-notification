package com.user.service.utils;

import autofixture.publicinterface.Any;
import com.user.service.dtos.UserDto;

public class UserDtoUtils {
    public static UserDto createUserDto() {
        return createUserDtoWithLogin(Any.string());
    }

    public static UserDto createUserDtoWithLogin(String login) {
        return UserDto.builder()
                .login(login)
                .name(Any.string())
                .lastName(Any.string())
                .build();
    }
}
