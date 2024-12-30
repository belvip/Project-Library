package com.library.system.exception.bookException;

import java.sql.SQLException;

public class BookGetByIdException extends Exception {
    // Constructeur acceptant le message d'erreur et la cause (SQLException)
    public BookGetByIdException(String message, SQLException cause) {
        super(message, cause);  // Passer le message et la cause à la classe Exception
    }
}

