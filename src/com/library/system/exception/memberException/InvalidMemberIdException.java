package com.library.system.exception.memberException;

// Exception for invalid member ID
public class InvalidMemberIdException extends RuntimeException {
    public InvalidMemberIdException(String message) {
        super(message);
    }
}
