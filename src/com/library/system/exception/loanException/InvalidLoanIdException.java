package com.library.system.exception.loanException;

public class InvalidLoanIdException extends RuntimeException{
    public InvalidLoanIdException(String message) {
        super(message);
    }
}
