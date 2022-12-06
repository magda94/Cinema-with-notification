package com.film.service.exceptions;

public class FilmWithIdExistException extends RuntimeException {

    public FilmWithIdExistException(String message) {
        super(message);
    }
}
