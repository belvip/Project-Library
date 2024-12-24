package com.library.system.exception.bookDaoException;

/**
 * Exception levée lorsqu'un livre ne peut pas être supprimé.
 */
public class BookRemoveException extends RuntimeException {
    public BookRemoveException(String message) {
        super(message);
    }
}

