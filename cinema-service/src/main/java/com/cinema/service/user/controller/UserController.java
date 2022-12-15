package com.cinema.service.user.controller;

import com.cinema.service.user.dto.UserRequest;
import com.cinema.service.user.dto.UserResponse;
import com.cinema.service.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@Slf4j
public class UserController {

    private final UserService userService;

    @PostMapping("/register")
    public ResponseEntity<UserResponse> registerUser(@RequestBody @Valid UserRequest userRequest) {
        log.info("Register user with login: '{}' request", userRequest.getLogin());
        return ResponseEntity.ok(userService.registerUser(userRequest));
    }
}
