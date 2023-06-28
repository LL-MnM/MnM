package com.example.MnM.boundedContext.chat.controller;

import com.example.MnM.boundedContext.chat.dto.ChatMessageDto;
import com.example.MnM.boundedContext.room.dto.DeleteRoomDto;
import com.example.MnM.boundedContext.chat.entity.ChatStatus;
import com.example.MnM.boundedContext.chat.service.ChatService;
import com.example.MnM.boundedContext.room.service.RoomService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

import static com.example.MnM.boundedContext.chat.entity.ChatStatus.*;

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

        switch (messageDto.getStatus()) {
            case EXIT -> {
                return isRoomOwnerExit(messageDto) ? deleteRoom(messageDto) : messageDto;
            }
            case SEND -> {
                chatService.saveChatToCache(roomId, messageDto);
            }
        }
        return messageDto;
    }

    @MessageMapping("/singleChat/{roomId}")
    @SendTo("/single/chat/{roomId}")
    public ChatMessageDto sendOneToOne(@DestinationVariable String roomId, ChatMessageDto messageDto) {

        chatService.saveChatToCache(roomId, messageDto);

        return isRoomOwnerExit(messageDto) ? deleteRoom(messageDto) : messageDto;
    }

    private boolean isRoomOwnerExit(ChatMessageDto messageDto) {
        return isExit(messageDto.getStatus()) &&
                roomService.isRoomOwner(messageDto.getRoomId(), messageDto.getSenderId());
    }

    private boolean isExit(ChatStatus status) {
        return status.equals(EXIT);
    }

    private ChatMessageDto deleteRoom(ChatMessageDto messageDto) {
        publisher.publishEvent(new DeleteRoomDto(messageDto.getRoomId(), messageDto.getSender(), messageDto.getSenderId()));
        messageDto.statusToDelete();
        return messageDto;
    }
}
