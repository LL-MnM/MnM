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
    private String senderName;
    private String message;
    private ChatStatus status;

    public void statusToDelete() {
        status = ChatStatus.DELETE;
    }

    public void addUserInfo(String sender) {
        this.senderName = sender;
    }
}
