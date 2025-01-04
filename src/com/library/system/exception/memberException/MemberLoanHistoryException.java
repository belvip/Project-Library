package com.library.system.exception.memberException;

public class MemberLoanHistoryException extends Exception {
    public MemberLoanHistoryException(String message) {
        super(message);
    }

    public MemberLoanHistoryException(String message, Throwable cause) {
        super(message, cause);
    }
}
