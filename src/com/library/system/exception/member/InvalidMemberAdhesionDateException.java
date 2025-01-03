package com.library.system.exception.member;

// Exception for invalid member adhesion date
public class InvalidMemberAdhesionDateException extends RuntimeException {
    public InvalidMemberAdhesionDateException(String message) {
        super(message);
    }
}