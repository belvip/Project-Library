package com.library.system.exception;

public class DatabaseNotFoundException extends RuntimeException{
    public DatabaseNotFoundException(String message) {
        super(message);
    }
}
