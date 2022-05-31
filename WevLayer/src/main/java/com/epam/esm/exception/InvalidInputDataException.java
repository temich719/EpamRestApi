package com.epam.esm.exception;

public class InvalidInputDataException extends Exception {

    private final String message;

    public InvalidInputDataException(String message) {
        super();
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

}
