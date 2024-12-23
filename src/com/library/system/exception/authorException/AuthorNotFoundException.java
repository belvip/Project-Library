package com.library.system.exception.authorException;

// Exception personnalisée pour gérer les cas où aucun auteur n'est trouvé
public class AuthorNotFoundException extends Exception {

    // Constructeur sans argument
    public AuthorNotFoundException() {
        super("Aucun auteur trouvé.");
    }

    // Constructeur avec un message personnalisé
    public AuthorNotFoundException(String message) {
        super(message);
    }

    // Constructeur avec un message personnalisé et une cause
    public AuthorNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    // Constructeur avec une cause
    public AuthorNotFoundException(Throwable cause) {
        super(cause);
    }
}
