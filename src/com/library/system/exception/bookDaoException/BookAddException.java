package com.library.system.exception.bookDaoException;

/**
 * Exception levée lorsqu'un livre ne peut pas être ajouté à la base de données.
 */
public class BookAddException extends RuntimeException {
    public BookAddException(String message) {
        super(message);
    }

    public BookAddException(String message, Throwable cause) {
        super(message, cause);
    }
}