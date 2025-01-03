package com.library.system.exception.member;

// Exception for invalid member email
public class InvalidMemberEmailException extends RuntimeException {
    public InvalidMemberEmailException(String message) {
        super(message);
    }
}