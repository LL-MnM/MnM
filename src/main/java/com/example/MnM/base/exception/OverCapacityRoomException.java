package com.example.MnM.base.exception;

public class OverCapacityRoomException extends RuntimeException {
    public OverCapacityRoomException() {
        super();
    }

    public OverCapacityRoomException(String message) {
        super(message);
    }

    public OverCapacityRoomException(String message, Throwable cause) {
        super(message, cause);
    }

    public OverCapacityRoomException(Throwable cause) {
        super(cause);
    }
}
