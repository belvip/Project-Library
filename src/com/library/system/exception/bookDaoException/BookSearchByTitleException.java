package com.library.system.exception.bookDaoException;

/**
 * Exception levée lorsqu'une erreur se produit lors de la recherche d'un livre par son titre.
 */
public class BookSearchByTitleException extends RuntimeException {

    // Constructeur avec message d'erreur
    public BookSearchByTitleException(String message) {
        super(message);
    }

    // Constructeur avec message et exception (Throwable)
    public BookSearchByTitleException(String message, Throwable cause) {
        super(message, cause);
    }
}
