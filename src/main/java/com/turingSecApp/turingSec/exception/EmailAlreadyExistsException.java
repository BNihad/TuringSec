package com.turingSecApp.turingSec.exception;

public class EmailAlreadyExistsException extends RuntimeException {

    private final String message;

    public EmailAlreadyExistsException(String message) {
        super(message);
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
