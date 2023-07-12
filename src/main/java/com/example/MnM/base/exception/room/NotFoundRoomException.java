package com.example.MnM.base.exception.room;

public class NotFoundRoomException extends RuntimeException {
    public NotFoundRoomException() {
        super();
    }

    public NotFoundRoomException(String message) {
        super(message);
    }

    public NotFoundRoomException(String message, Throwable cause) {
        super(message, cause);
    }

    public NotFoundRoomException(Throwable cause) {
        super(cause);
    }
}
