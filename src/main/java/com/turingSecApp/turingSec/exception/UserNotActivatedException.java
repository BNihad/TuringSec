package com.turingSecApp.turingSec.exception;

public class UserNotActivatedException extends RuntimeException{

    private final String message;

    public UserNotActivatedException(String message) {
        super(message);
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
