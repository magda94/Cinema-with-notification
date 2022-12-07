package com.film.service.exceptions;

public class CommentWithIdExistException extends RuntimeException {

    public CommentWithIdExistException(String message) {
        super(message);
    }
}
