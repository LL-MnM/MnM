package com.example.MnM.boundedContext.mbtiboard.entity.mbtiquestion;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "entity not found")
public class MbtiDataNotFoundException extends RuntimeException {
    public MbtiDataNotFoundException(String msg) {
        super(msg);
    }
}