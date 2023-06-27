package com.example.MnM.base.exception;

public class NotMatchUserLevelException extends RuntimeException {
    public NotMatchUserLevelException() {
    }

    public NotMatchUserLevelException(String message) {
        super(message);
    }

    public NotMatchUserLevelException(String message, Throwable cause) {
        super(message, cause);
    }

    public NotMatchUserLevelException(Throwable cause) {
        super(cause);
    }

    public NotMatchUserLevelException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}