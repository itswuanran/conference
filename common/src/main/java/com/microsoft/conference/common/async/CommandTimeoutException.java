package com.microsoft.conference.common.async;

public class CommandTimeoutException extends RuntimeException {
    public CommandTimeoutException(String message, Throwable cause) {
        super(message, cause);
    }
}
