package com.example.MnM.boundedContext.chat.dto;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.time.LocalDateTime;

@EqualsAndHashCode
@AllArgsConstructor
@Getter
public class ChatMessageDto {

    private String roomId;
    private String writer;
    private String message;
    private LocalDateTime createDate;

}
