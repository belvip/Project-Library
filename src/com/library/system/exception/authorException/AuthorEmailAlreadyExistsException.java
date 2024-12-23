package com.library.system.exception.authorException;

public class AuthorEmailAlreadyExistsException extends RuntimeException {
    public AuthorEmailAlreadyExistsException(String message) {
        super(message);
    }
}
