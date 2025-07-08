package com.TicketXChange.TicketXChange.auth.exception;

public class SameUserConnectionException extends RuntimeException {
    public SameUserConnectionException(String message) {
        super(message);
    }
}
