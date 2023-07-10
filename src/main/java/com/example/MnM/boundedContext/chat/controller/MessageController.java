package com.example.MnM.boundedContext.chat.controller;

import com.example.MnM.boundedContext.chat.dto.ChatMessageDto;
import com.example.MnM.boundedContext.chat.dto.SaveChatDto;
import com.example.MnM.boundedContext.chat.entity.ChatStatus;
import com.example.MnM.boundedContext.chat.service.ChatService;
import com.example.MnM.boundedContext.room.service.RoomService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageExceptionHandler;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

import java.security.Principal;

import static com.example.MnM.boundedContext.chat.entity.ChatStatus.EXIT;

@Slf4j
@RequiredArgsConstructor
@Controller
public class MessageController {

    private final ChatService chatService;
    private final RoomService roomService;
    private final ApplicationEventPublisher publisher;

    @MessageMapping("/chat/{roomId}")
    @SendTo("/group/chat/{roomId}")
    public ChatMessageDto sendGroup(@DestinationVariable String roomId, ChatMessageDto messageDto,
                                    Principal principal) {

        messageDto.addUserInfo(principal.getName());
        switch (messageDto.getStatus()) {
            case EXIT -> {

                if (isRoomOwnerExit(messageDto)) {
                    return finishGroupChat(messageDto);
                }

            }
            case SEND -> {
                roomService.checkSingleRoomParticipants(roomId, messageDto.getSenderName());
            }
        }
        return messageDto;
    }

    @MessageMapping("/singleChat/{roomId}")
    @SendTo("/single/chat/{roomId}")
    public ChatMessageDto sendOneToOne(@DestinationVariable String roomId, ChatMessageDto messageDto,
                                       Principal principal) {

        messageDto.addUserInfo(principal.getName());
        switch (messageDto.getStatus()) {
            case EXIT -> {

                if (isRoomOwnerExit(messageDto)) {
                    return finishSingleChat(messageDto);
                }

            }
            case SEND -> {
                roomService.checkSingleRoomParticipants(roomId, messageDto.getSenderName());
                chatService.saveChatToCache(roomId, messageDto);
            }
        }
        return messageDto;
    }


    @MessageExceptionHandler(Exception.class)
    public void messageExceptionHandler(Exception e) {
        log.info("메시지 에러 발생", e);
    }

    private boolean isRoomOwnerExit(ChatMessageDto messageDto) {
        return isExit(messageDto.getStatus()) &&
                roomService.isRoomOwner(messageDto.getRoomId(), messageDto.getSenderName());
    }

    private boolean isExit(ChatStatus status) {
        return status.equals(EXIT);
    }

    private ChatMessageDto finishSingleChat(ChatMessageDto messageDto) {
        publisher.publishEvent(new SaveChatDto(messageDto.getRoomId()));
        roomService.deleteDbRoom(messageDto.getRoomId());
        messageDto.statusToDelete();
        return messageDto;
    }

    private ChatMessageDto finishGroupChat(ChatMessageDto messageDto) {
        roomService.exitRoom(messageDto.getRoomId(), messageDto.getSenderName());
        roomService.deleteDbRoom(messageDto.getRoomId());
        roomService.deleteCacheRoom(messageDto.getRoomId());
        messageDto.statusToDelete();
        return messageDto;
    }
}
