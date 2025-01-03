package com.library.system.exception.member;

// Exception for invalid member name (first or last name)
public class InvalidMemberNameException extends RuntimeException {
    public InvalidMemberNameException(String message) {
        super(message);
    }
}
