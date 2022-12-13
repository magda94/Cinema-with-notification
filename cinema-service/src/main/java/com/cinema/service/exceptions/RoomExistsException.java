package com.cinema.service.exceptions;

public class RoomExistsException extends RuntimeException {

    public RoomExistsException(String message) {
        super(message);
    }
}
