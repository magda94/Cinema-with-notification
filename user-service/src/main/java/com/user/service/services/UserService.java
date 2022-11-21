package com.user.service.services;

import com.user.service.dtos.UserDto;
import com.user.service.entity.UserEntity;
import com.user.service.exceptions.UserNotFoundException;
import com.user.service.repository.UserEntityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserEntityRepository userEntityRepository;

    public List<UserDto> getUserList() {
        return userEntityRepository.findAll()
                .stream()
                .map(UserEntity::toDto)
                .collect(Collectors.toList());
    }

    public UserDto getUserWithLogin(String login) {
        return userEntityRepository.findByLogin(login)
                .map(UserEntity::toDto)
                .orElseThrow(() ->
                        new UserNotFoundException("Cannot find user with login: " + login));
    }

    public UserDto createUser(UserDto userDto) {
        return null;
    }

    public UserDto updateUser(UserDto userDto) {
        return null;
    }

    public void deleteUser(String login) {
    }
}
