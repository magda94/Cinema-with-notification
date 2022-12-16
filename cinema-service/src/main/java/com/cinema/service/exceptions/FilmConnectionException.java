package com.cinema.service.exceptions;

import lombok.Getter;
import org.springframework.http.HttpStatus;

public class FilmConnectionException extends RuntimeException {

    @Getter
    private HttpStatus httpStatus;

    public FilmConnectionException(String message, HttpStatus httpStatus) {
        super(message);
        this.httpStatus = httpStatus;
    }
}
