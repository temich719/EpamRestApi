package com.epam.esm.exception;

public class NotInsertedException extends Exception {

    public NotInsertedException() {
        super();
    }

    public String getMessage() {
        return "Cannot insert object";
    }
}
