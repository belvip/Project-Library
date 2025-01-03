package com.library.system.exception.memberException;

// Exception for invalid member adhesion date
public class InvalidMemberAdhesionDateException extends RuntimeException {
    public InvalidMemberAdhesionDateException(String message) {
        super(message);
    }
}