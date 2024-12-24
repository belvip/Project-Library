package com.library.system.exception.bookDaoException;

/**
 * Exception levée lorsqu'une erreur se produit lors de la recherche d'un livre par titre.
 */
public class BookSearchByTitleException extends RuntimeException {
    public BookSearchByTitleException(String message) {
        super(message);
    }
}

