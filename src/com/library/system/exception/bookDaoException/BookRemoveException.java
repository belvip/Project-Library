package com.library.system.exception.bookDaoException;

/**
 * Exception levée lorsqu'une erreur se produit lors de la suppression d'un livre.
 */
public class BookRemoveException extends RuntimeException {

    // Constructeur avec message d'erreur
    public BookRemoveException(String message) {
        super(message);
    }

    // Constructeur avec message et exception (Throwable)
    public BookRemoveException(String message, Throwable cause) {
        super(message, cause);
    }
}
