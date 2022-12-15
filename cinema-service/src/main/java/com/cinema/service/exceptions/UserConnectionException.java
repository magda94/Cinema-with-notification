package com.cinema.service.exceptions;

import lombok.Getter;
import org.springframework.http.HttpStatus;

public class UserConnectionException extends RuntimeException {

    @Getter
    private HttpStatus httpStatus;

    public UserConnectionException(String message, HttpStatus httpStatus) {
        super(message);
        this.httpStatus = httpStatus;
    }
}
