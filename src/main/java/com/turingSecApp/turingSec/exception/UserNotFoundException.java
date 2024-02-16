package com.turingSecApp.turingSec.exception;

public class UserNotFoundException extends  RuntimeException {


    private final String message;

    public UserNotFoundException(String message) {
        super(message);
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
