package com.library.system.exception.bookDaoException;

/**
 * Exception levée lorsqu'une erreur se produit lors de la mise à jour d'un livre.
 */
public class BookUpdateException extends RuntimeException {
    public BookUpdateException(String message) {
        super(message);
    }
}
