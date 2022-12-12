package com.cinema.service.exceptions;

public class TicketExistsException extends RuntimeException {

    public TicketExistsException(String message) {
        super(message);
    }
}
