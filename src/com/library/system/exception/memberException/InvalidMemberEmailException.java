package com.library.system.exception.memberException;

// Exception for invalid member email
public class InvalidMemberEmailException extends RuntimeException {
    public InvalidMemberEmailException(String message) {
        super(message);
    }
}