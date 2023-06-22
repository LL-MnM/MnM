package com.example.MnM.boundedContext.chat.dto;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class ChatMessageDto {

    private String roomId;
    private String sender;
    private String message;
}
