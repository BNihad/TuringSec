package com.turingSecApp.turingSec.exception;

public class UnauthorizedException extends  RuntimeException{

    private final String message;

    public UnauthorizedException(String message) {
        super(message);
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
