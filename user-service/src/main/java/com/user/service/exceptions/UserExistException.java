package com.user.service.exceptions;

public class UserExistException extends RuntimeException {

    public UserExistException() {
        super();
    }

    public UserExistException(String message) {
        super(message);
    }
}
