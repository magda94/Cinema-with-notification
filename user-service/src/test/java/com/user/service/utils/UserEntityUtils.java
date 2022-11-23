package com.user.service.utils;


import autofixture.publicinterface.Any;
import com.user.service.dtos.UserDto;
import com.user.service.entity.UserEntity;

import java.util.UUID;

public class UserEntityUtils {

    public static UserEntity createUserEntity() {
        return UserEntity.builder()
                .uuid(UUID.randomUUID().toString())
                .login(Any.string())
                .name(Any.string())
                .lastName(Any.string())
                .build();
    }

    public static UserEntity createUserEntityFromDto(UserDto userDto) {
        return UserEntity.builder()
                .uuid(UUID.randomUUID().toString())
                .login(userDto.getLogin())
                .name(userDto.getName())
                .lastName(userDto.getLastName())
                .build();
    }
}
