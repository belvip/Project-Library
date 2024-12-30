package com.library.system.exception.bookDaoException;

public class BookGetByIdException extends RuntimeException{
    public BookGetByIdException(String message) {
        super(message);
    }
}
