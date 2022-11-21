package com.user.service.controllers;

import com.user.service.dtos.UserDto;
import com.user.service.services.UserDtoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@Slf4j
public class UserDtoController {

    private final UserDtoService userService;

    @GetMapping("")
    public ResponseEntity<List<UserDto>> getUsers() {
        log.info("Get all users request");
        return ResponseEntity.ok(userService.getUserList());
    }

    @GetMapping("/{login}")
    public ResponseEntity<UserDto> getUserWithLogin(@PathVariable String login) {
        log.info("Get user with login: {} request", login);
        return ResponseEntity.ok(userService.getUserWithLogin(login));
    }

    @PostMapping
    public ResponseEntity<UserDto> createUser(@RequestBody UserDto user) {
        log.info("Create user with login: {} request", user.getLogin());
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(userService.createUser(user));
    }

    @PutMapping("/{login}")
    public ResponseEntity<UserDto> updateUser(@PathVariable String login, @RequestBody UserDto userDto) {
        log.info("Update user with login: {} request", login);
        return ResponseEntity.status(HttpStatus.OK)
                .body(userService.updateUser(login, userDto));
    }

    @DeleteMapping("/{login}")
    public ResponseEntity deleteUser(@PathVariable String login) {
        log.info("Remove user with login: {}, login");
        userService.deleteUser(login);
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
