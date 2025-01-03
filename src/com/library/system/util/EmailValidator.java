package com.library.system.util;

public class EmailValidator {
    private static final String EMAIL_PATTERN = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";

    public static boolean isValid(String email) {
        return email != null && email.matches(EMAIL_PATTERN);
    }
}
