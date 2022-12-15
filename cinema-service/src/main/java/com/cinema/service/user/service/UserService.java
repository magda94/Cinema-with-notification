package com.cinema.service.user.service;

import com.cinema.service.exceptions.UserConnectionException;
import com.cinema.service.user.dto.UnregisterUserRequest;
import com.cinema.service.user.dto.UserRequest;
import com.cinema.service.user.dto.UserResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {

    private final RestTemplate restTemplate;

    @Value("${com.cinema.service.user}")
    private String userHost;

    public UserResponse registerUser(UserRequest userRequest) {
        String url = "http://" + userHost + "/users";

        try {
            var result = restTemplate.postForEntity(url, userRequest, UserRequest.class);

            if (result.getStatusCode().is2xxSuccessful()) {
                return UserResponse.builder()
                        .login(result.getBody().getLogin())
                        .build();
            } else {
                log.error("Error during connection with user service. Http status: {}", result.getStatusCode());
                throw new UserConnectionException("Cannot register user", result.getStatusCode());
            }
        }catch (Exception ex) {
            log.error("Error during connection with user service. Message: {}", ex.getMessage());
            throw new UserConnectionException("Cannot register user", HttpStatus.FORBIDDEN);
        }
    }

    public void unregisterUser(UnregisterUserRequest userRequest) {
        String url = "http://" + userHost + "/users/" + userRequest.getLogin();

        try {
            restTemplate.delete(url);
        } catch (Exception ex) {
            log.error("Error during connection with user service. Message: {}", ex.getMessage());
            throw new UserConnectionException("Cannot unregister user", HttpStatus.FORBIDDEN);
        }
    }
}
