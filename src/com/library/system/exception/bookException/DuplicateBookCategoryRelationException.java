package com.library.system.exception.bookException;

public class DuplicateBookCategoryRelationException extends RuntimeException {
    public DuplicateBookCategoryRelationException(String message) {
        super(message);
    }
}