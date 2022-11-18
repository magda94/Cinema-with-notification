package com.user.service.dtos;

import com.user.service.entity.UserEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDto {

    private String login;
    private String name;
    private String lastName;

    public UserDto toDto(UserEntity userEntity) {
        return UserDto.builder()
                .login(userEntity.getLogin())
                .name(userEntity.getName())
                .lastName(userEntity.getLastName())
                .build();
    }
}
