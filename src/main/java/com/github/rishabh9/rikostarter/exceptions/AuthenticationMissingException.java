package com.github.rishabh9.rikostarter.exceptions;

public class AuthenticationMissingException extends RuntimeException {
    public AuthenticationMissingException(String message) {
        super(message);
    }

    public AuthenticationMissingException(String message, Throwable cause) {
        super(message, cause);
    }
}
