package com.user.service.services;

import com.user.service.dtos.UserDto;
import com.user.service.entity.UserEntity;
import com.user.service.exceptions.UserExistException;
import com.user.service.exceptions.UserNotFoundException;
import com.user.service.repository.UserEntityRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserDtoService {

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
        userEntityRepository.findByLogin(userDto.getLogin())
                        .ifPresent(s -> new UserExistException(String.format("User with login: %s exists", userDto.getLogin())));

        var userEntity = UserEntity.builder()
                .uuid(UUID.randomUUID().toString())
                .login(userDto.getLogin())
                .name(userDto.getName())
                .lastName(userDto.getLastName())
                .build();

        var saved = userEntityRepository.save(userEntity);
        log.info("Created user - UUID({})", saved.getUuid());
        return saved.toDto();
    }

    public UserDto updateUser(String login, UserDto userDto) {
        var userEntity = findByLogin(login);

        userEntityRepository.findByLogin(userDto.getLogin())
                .ifPresent(s -> {
                    if (!login.equals(userDto.getLogin())) {
                        throw new UserExistException(String.format("User with login: %s exist in database", userDto.getLogin()));
                    }
                });

        var toSaved = UserEntity.builder()
                .uuid(userEntity.getUuid())
                .login(userDto.getLogin())
                .name(userDto.getName())
                .lastName(userDto.getLastName())
                .build();

        var saved = userEntityRepository.save(toSaved);
        log.info("Updated user - UUID({})", saved.getUuid());
        return saved.toDto();
    }

    public void deleteUser(String login) {
        var userEntity = findByLogin(login);
        userEntityRepository.delete(userEntity);
        log.info("Deleted user - UUID({})", userEntity.getUuid());
    }

    private UserEntity findByLogin(String login) {
        return userEntityRepository.findByLogin(login)
                .orElseThrow(() ->
                        new UserNotFoundException("Cannot find user with login: " + login));
    }
}
