package com.library.system.exception.bookException;

public class InvalidBookIdException extends RuntimeException{
    public InvalidBookIdException(String message) {
        super(message);
    }
}
