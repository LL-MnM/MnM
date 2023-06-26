package com.example.MnM.base.exception;

public class NotValidRoomException extends RuntimeException{
    public NotValidRoomException() {
        super();
    }

    public NotValidRoomException(String message) {
        super(message);
    }

    public NotValidRoomException(String message, Throwable cause) {
        super(message, cause);
    }

    public NotValidRoomException(Throwable cause) {
        super(cause);
    }
}
