package com.library.system.exception.bookDaoException;

public class BookUpdateException extends RuntimeException {
    // Constructeur qui prend un message et une exception
    public BookUpdateException(String message, Exception cause) {
        super(message, cause); // Passe le message et la cause à la classe parente RuntimeException
    }

    // Constructeur qui prend uniquement un message
    public BookUpdateException(String message) {
        super(message); // Passe uniquement le message à la classe parente RuntimeException
    }
}
