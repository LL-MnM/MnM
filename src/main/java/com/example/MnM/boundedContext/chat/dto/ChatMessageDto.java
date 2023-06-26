package com.example.MnM.boundedContext.chat.dto;

import com.example.MnM.boundedContext.chat.entity.ChatStatus;
import lombok.*;

@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class ChatMessageDto {

    private String roomId;
    private String sender;
    private Long senderId;
    private String message;
    private ChatStatus status;

    public void updateStatus() {
        status = ChatStatus.SEND;
    }
}
