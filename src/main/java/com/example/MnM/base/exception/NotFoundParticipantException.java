package com.example.MnM.base.exception;

public class NotFoundParticipantException extends RuntimeException {
    public NotFoundParticipantException() {
        super();
    }

    public NotFoundParticipantException(String message) {
        super(message);
    }

    public NotFoundParticipantException(String message, Throwable cause) {
        super(message, cause);
    }

    public NotFoundParticipantException(Throwable cause) {
        super(cause);
    }
}
