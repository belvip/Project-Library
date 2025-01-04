package com.library.system.exception.loanException;

public class RegisterLoanException extends Exception {

    // Constructeur avec message d'erreur
    public RegisterLoanException(String message) {
        super(message);
    }

    // Constructeur avec message d'erreur et exception cause (pour chaîner l'exception)
    public RegisterLoanException(String message, Throwable cause) {
        super(message, cause);
    }
}
