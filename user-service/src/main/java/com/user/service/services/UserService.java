package com.user.service.services;

import com.user.service.dtos.UserDto;
import com.user.service.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public List<UserDto> getUserList() {
        return null;
    }

    public UserDto getUserWithLogin(String login) {
        return null;
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
