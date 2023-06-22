package com.example.MnM.boundedContext.chat.controller;

import com.example.MnM.boundedContext.chat.dto.ChatMessageDto;
import com.example.MnM.boundedContext.chat.service.ChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@RequiredArgsConstructor
@Controller
public class MessageController {

    private final ChatService chatService;

    @MessageMapping("/chat/{roomId}")
    @SendTo("/sub/chat/{roomId}")
    public ChatMessageDto sendManyToMany(@DestinationVariable String roomId, ChatMessageDto messageDto) {
        chatService.saveChat(roomId, messageDto);
        return messageDto;
    }

    @MessageMapping("/chatOne/{roomId}")
    @SendTo("/single/chat/{roomId}")
    public ChatMessageDto sendOneToOne(@DestinationVariable String roomId, ChatMessageDto messageDto) {
        chatService.saveChat(roomId, messageDto);
        return messageDto;
    }
}
