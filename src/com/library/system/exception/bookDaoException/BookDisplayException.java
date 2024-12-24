package com.library.system.exception.bookDaoException;

/**
 * Exception levée lorsqu'une erreur se produit lors de l'affichage des livres disponibles.
 */
public class BookDisplayException extends RuntimeException {
    public BookDisplayException(String message) {
        super(message);
    }
}