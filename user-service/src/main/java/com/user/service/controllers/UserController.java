package com.user.service.controllers;

import com.user.service.dtos.UserDto;
import com.user.service.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/")
    public ResponseEntity<List<UserDto>> getUsers() {
        return null;
    }

    @GetMapping("/{login}")
    public ResponseEntity<UserDto> getUserWithLogin(@PathVariable String login) {
        return null;
    }

    @PostMapping("/")
    public ResponseEntity<UserDto> createUser(@RequestBody UserDto user) {
        return null;
    }

    @PutMapping("/{login}")
    public ResponseEntity<UserDto> updateUser(@RequestBody UserDto user) {
        return null;
    }

    @DeleteMapping("/{login}")
    public ResponseEntity deleteUser(@PathVariable String login) {
        return null;
    }
}
