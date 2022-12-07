package com.film.service.exceptions;

public class ForbiddenParameterChangedException extends RuntimeException {

    public ForbiddenParameterChangedException(String message) {
        super(message);
    }
}
