package com.example.MnM.boundedContext.chat.controller;

import com.example.MnM.boundedContext.chat.dto.ChatMessageDto;
import com.example.MnM.boundedContext.chat.dto.DeleteRoomDto;
import com.example.MnM.boundedContext.chat.entity.ChatStatus;
import com.example.MnM.boundedContext.chat.service.ChatService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Slf4j
@RequiredArgsConstructor
@Controller
public class MessageController {

    private final ChatService chatService;
    private final ApplicationEventPublisher publisher;

    @MessageMapping("/chat/{roomId}")
    @SendTo("/sub/chat/{roomId}")
    public ChatMessageDto sendManyToMany(@DestinationVariable String roomId, ChatMessageDto messageDto) {
        chatService.saveChat(roomId, messageDto);

        if (isExitRoomOwner(messageDto)) {
            publisher.publishEvent(new DeleteRoomDto(messageDto.getRoomId(),messageDto.getSender()));
            return messageDto;
        }

        messageDto.isNotOwner();
        return messageDto;
    }

    private  boolean isExitRoomOwner(ChatMessageDto messageDto) {
        return messageDto.getStatus().equals(ChatStatus.EXIT);
    }

    @MessageMapping("/chatOne/{roomId}")
    @SendTo("/single/chat/{roomId}")
    public ChatMessageDto sendOneToOne(@DestinationVariable String roomId, ChatMessageDto messageDto) {
        chatService.saveChat(roomId, messageDto);
        return messageDto;
    }
}
