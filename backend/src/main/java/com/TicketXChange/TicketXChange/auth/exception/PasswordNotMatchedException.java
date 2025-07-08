package com.TicketXChange.TicketXChange.auth.exception;

public class PasswordNotMatchedException extends Exception {
    public PasswordNotMatchedException(String wrong_password) {
        super(wrong_password);
    }
}
