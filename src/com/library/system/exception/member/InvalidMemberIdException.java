package com.library.system.exception.member;

// Exception for invalid member ID
public class InvalidMemberIdException extends RuntimeException {
    public InvalidMemberIdException(String message) {
        super(message);
    }
}
