package com.library.system.exception.bookDaoException;

/**
 * Exception levée lorsqu'une erreur se produit lors de la vérification de la disponibilité d'un livre.
 */
public class BookAvailabilityException extends RuntimeException {

    // Constructeur avec message d'erreur
    public BookAvailabilityException(String message) {
        super(message);
    }

    // Constructeur avec message et exception (Throwable)
    public BookAvailabilityException(String message, Throwable cause) {
        super(message, cause);
    }
}
