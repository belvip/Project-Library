package com.library.system.exception.bookDaoException;

/**
 * Exception levée lorsqu'une erreur se produit lors de la vérification de la disponibilité d'un livre.
 */
public class BookAvailabilityException extends RuntimeException {
    public BookAvailabilityException(String message) {
        super(message);
    }
}

