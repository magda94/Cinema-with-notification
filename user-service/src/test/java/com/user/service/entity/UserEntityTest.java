package com.user.service.entity;

import com.user.service.dtos.UserDto;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class UserEntityTest {

    @Test
    public void shouldCreateUserDto() {
        var login = "Login";
        var name = "Name";
        var lastName = "LastName";
        UserEntity userEntity = UserEntity.builder()
                .uuid(UUID.randomUUID().toString())
                .login(login)
                .name(name)
                .lastName(lastName)
                .build();

        UserDto userDto = UserDto.builder()
                .login(login)
                .name(name)
                .lastName(lastName)
                .build();

        assertThat(userDto).isEqualTo(userEntity.toDto());
    }

}