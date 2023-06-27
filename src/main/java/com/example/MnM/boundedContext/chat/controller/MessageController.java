package com.example.MnM.boundedContext.chat.controller;

import com.example.MnM.boundedContext.chat.dto.ChatMessageDto;
import com.example.MnM.boundedContext.chat.dto.DeleteRoomDto;
import com.example.MnM.boundedContext.chat.entity.ChatStatus;
import com.example.MnM.boundedContext.chat.service.ChatService;
import com.example.MnM.boundedContext.chat.service.RoomService;
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
    private final RoomService roomService;
    private final ApplicationEventPublisher publisher;

    @MessageMapping("/chat/{roomId}")
    @SendTo("/group/chat/{roomId}")
    public ChatMessageDto sendGroup(@DestinationVariable String roomId, ChatMessageDto messageDto) {

        chatService.saveChat(roomId, messageDto);

        return isRoomOwnerExit(messageDto) ? deleteRoom(messageDto) : messageDto;
    }

    @MessageMapping("/singleChat/{roomId}")
    @SendTo("/single/chat/{roomId}")
    public ChatMessageDto sendOneToOne(@DestinationVariable String roomId, ChatMessageDto messageDto) {

        chatService.saveChat(roomId, messageDto);

        return isRoomOwnerExit(messageDto) ? deleteRoom(messageDto) : messageDto;
    }

    private boolean isRoomOwnerExit(ChatMessageDto messageDto) {
        return messageDto.getStatus().equals(ChatStatus.EXIT) && isExitRoomOwner(messageDto);
    }

    private ChatMessageDto deleteRoom(ChatMessageDto messageDto) {
        publisher.publishEvent(new DeleteRoomDto(messageDto.getRoomId(), messageDto.getSender(), messageDto.getSenderId()));
        messageDto.statusToDelete();
        return messageDto;
    }

    private boolean isExitRoomOwner(ChatMessageDto messageDto) {
        return roomService.isRoomOwner(messageDto.getRoomId(), messageDto.getSenderId());
    }
}
