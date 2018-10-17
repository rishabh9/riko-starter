package com.github.rishabh9.rikostarter.exceptions;

public class WebSocketError extends Exception {
    public WebSocketError(String message) {
        super(message);
    }

    public WebSocketError(String message, Throwable cause) {
        super(message, cause);
    }
}
