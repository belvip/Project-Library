package com.library.system.exception.authorException;

public class AuthorDeleteException extends Exception {

    // Constructeur sans argument
    public AuthorDeleteException() {
        super("Erreur lors de la suppression de l'auteur.");
    }

    // Constructeur avec message personnalisé
    public AuthorDeleteException(String message) {
        super(message);
    }

    // Constructeur avec message et cause
    public AuthorDeleteException(String message, Throwable cause) {
        super(message, cause);
    }

    // Constructeur avec une cause
    public AuthorDeleteException(Throwable cause) {
        super(cause);
    }
}
