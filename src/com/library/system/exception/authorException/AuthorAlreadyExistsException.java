package com.library.system.exception.authorException;

public class AuthorAlreadyExistsException extends Exception {
    // Constructeur sans argument
    public AuthorAlreadyExistsException() {
        super("L'auteur existe déjà.");
    }

    // Constructeur avec un message personnalisé
    public AuthorAlreadyExistsException(String message) {
        super(message);
    }

    // Constructeur avec un message personnalisé et une cause
    public AuthorAlreadyExistsException(String message, Throwable cause) {
        super(message, cause);
    }

    // Constructeur avec une cause
    public AuthorAlreadyExistsException(Throwable cause) {
        super(cause);
    }
}
