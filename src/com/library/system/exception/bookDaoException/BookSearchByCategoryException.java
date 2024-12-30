package com.library.system.exception.bookDaoException;

public class BookSearchByCategoryException extends Exception {
    public BookSearchByCategoryException(String message) {
        super(message);
    }

    public BookSearchByCategoryException(String message, Throwable cause) {
        super(message, cause);
    }
}
