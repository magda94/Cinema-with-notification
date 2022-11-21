package com.user.service.controllers;

import com.user.service.dtos.UserDto;
import com.user.service.services.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@Slf4j
public class UserController {

    private final UserService userService;

    @GetMapping("")
    public ResponseEntity<List<UserDto>> getUsers() {
        log.info("IN");
        return ResponseEntity.ok(userService.getUserList());
    }

    @GetMapping("/{login}")
    public ResponseEntity<UserDto> getUserWithLogin(@PathVariable String login) {
        return ResponseEntity.ok(userService.getUserWithLogin(login));
    }

    @PostMapping
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
