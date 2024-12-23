package com.library.system.exception.authorException;

public class InvalidAuthorEmailException extends RuntimeException {
    public InvalidAuthorEmailException(String message) {
        super(message);
    }
}
