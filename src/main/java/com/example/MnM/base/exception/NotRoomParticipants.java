package com.example.MnM.base.exception;

public class NotRoomParticipants extends RuntimeException {

    public NotRoomParticipants() {
        super();
    }

    public NotRoomParticipants(String message) {
        super(message);
    }

    public NotRoomParticipants(String message, Throwable cause) {
        super(message, cause);
    }

    public NotRoomParticipants(Throwable cause) {
        super(cause);
    }
}
