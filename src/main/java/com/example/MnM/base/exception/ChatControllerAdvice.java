package com.example.MnM.base.exception;

import com.example.MnM.base.exception.room.*;
import com.example.MnM.base.rq.Rq;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.Arrays;

@Slf4j
@RequiredArgsConstructor
@ControllerAdvice
public class ChatControllerAdvice {

    private final Rq rq;

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler({NotFoundRoomException.class, NotOwnerRoomException.class})
    public String notFoundRoomException(NotFoundRoomException e, HttpServletRequest request) {
        log.info("룸 권한 및 생성 오류 발생 --> ip = {}", getClientIP(request), e);
        return rq.historyBack(e.getMessage());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(OverCapacityRoomException.class)
    public String overCapacityRoomException(OverCapacityRoomException e, HttpServletRequest request) {
        log.info("방 인원 초과 예외 발생 --> ip = {}", getClientIP(request), e);
        return rq.historyBack(e.getMessage());
    }

    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ExceptionHandler({NotRoomParticipants.class, NotFoundParticipantException.class})
    public String notRoomParticipants(NotFoundRoomException e, HttpServletRequest request) {
        log.info("방 참여자 예외 발생 --> ip = {}", getClientIP(request), e);
        return rq.historyBack(e.getMessage());
    }

    public String getClientIP(HttpServletRequest request) {
        String[] headers = {"Proxy-Client-IP", "WL-Proxy-Client-IP", "HTTP_CLIENT_IP", "HTTP_X_FORWARDED_FOR"};
        return Arrays.stream(headers)
                .map(request::getHeader)
                .filter(i -> i != null && !i.isEmpty() && !"unknown".equalsIgnoreCase(i))
                .findFirst()
                .orElse(request.getRemoteAddr());
    }


}
