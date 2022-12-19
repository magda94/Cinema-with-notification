package com.cinema.service.exceptions;

public class TicketMismatchException extends RuntimeException {

    public TicketMismatchException(String message) {
        super(message);
    }
}
