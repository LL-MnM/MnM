package com.example.MnM.base.exception;

public class NotOwnerRoomException extends RuntimeException{
    public NotOwnerRoomException() {
        super();
    }

    public NotOwnerRoomException(String message) {
        super(message);
    }

    public NotOwnerRoomException(String message, Throwable cause) {
        super(message, cause);
    }

    public NotOwnerRoomException(Throwable cause) {
        super(cause);
    }
}
