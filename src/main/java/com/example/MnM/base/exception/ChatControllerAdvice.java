package com.example.MnM.base.exception;

import com.example.MnM.base.rq.Rq;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@RequiredArgsConstructor
@ControllerAdvice
public class ChatControllerAdvice {

    private final Rq rq;

    @ExceptionHandler(NotFoundRoomException.class)
    public String notFoundRoomException(NotFoundRoomException e) {
        return rq.historyBack(e.getMessage());
    }

    @ExceptionHandler(OverCapacityRoomException.class)
    public String overCapacityRoomException(OverCapacityRoomException e) {
        return rq.historyBack(e.getMessage());
    }
}
