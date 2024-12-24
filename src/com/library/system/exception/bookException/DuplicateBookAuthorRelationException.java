package com.library.system.exception.bookException;

public class DuplicateBookAuthorRelationException extends RuntimeException {
    public DuplicateBookAuthorRelationException(String message) {
        super(message);
    }
}
