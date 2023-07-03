package com.example.MnM.base.exception;

public class InvalidImageFormatException extends RuntimeException {
    public InvalidImageFormatException() {
        super();
    }

    public InvalidImageFormatException(String message) {
        super(message);
    }

    public InvalidImageFormatException(String message, Throwable cause) {
        super(message, cause);
    }

    public InvalidImageFormatException(Throwable cause) {
        super(cause);
    }
}
