package com.example.MnM.boundedContext.chat.dto;

import lombok.*;

@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class ChatMessageDto {

    private String roomId;
    private String sender;
    private String message;
}
