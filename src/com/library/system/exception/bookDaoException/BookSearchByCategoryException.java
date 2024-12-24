package com.library.system.exception.bookDaoException;

/**
 * Exception levée lorsqu'une erreur se produit lors de la recherche d'un livre par catégorie.
 */
public class BookSearchByCategoryException extends RuntimeException {
    public BookSearchByCategoryException(String message) {
        super(message);
    }
}
