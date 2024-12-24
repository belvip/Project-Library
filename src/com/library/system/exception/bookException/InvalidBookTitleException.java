package com.library.system.exception.bookException;

public class InvalidBookTitleException extends RuntimeException {
    public InvalidBookTitleException(String message) {
        super(message);
    }
}
