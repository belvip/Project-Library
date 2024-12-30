package com.library.system.exception.bookDaoException;

public class BookGetByIdServiceException extends RuntimeException{
    public BookGetByIdServiceException(String message) {
        super(message);
    }
}
