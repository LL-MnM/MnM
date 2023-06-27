package com.example.MnM.boundedContext.chat.controller;

import com.example.MnM.base.rq.Rq;
import com.example.MnM.boundedContext.chat.dto.DeleteRoomDto;
import com.example.MnM.boundedContext.chat.entity.ChatRoom;
import com.example.MnM.boundedContext.chat.entity.RoomStatus;
import com.example.MnM.boundedContext.chat.service.RoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@PreAuthorize("isAuthenticated()")
@RequiredArgsConstructor
@RequestMapping("/chat")
@Controller
public class RoomController {

    private final RoomService roomService;
    private final Rq rq;

    @GetMapping("/rooms")
    public String roomList(Model model) {
        List<ChatRoom> rooms = roomService.findAll();
        model.addAttribute("rooms",rooms);

        return "chat/rooms";
    }

    @PostMapping("/create/room/group")
    public String createGroupRoom() {
        String roomId = roomService.createRoom(rq.getMember().getId(),rq.getMember().getUsername(), RoomStatus.GROUP);

        return rq.redirectWithMsg("/chat/room/%s".formatted(roomId),"채팅 방이 생성되었습니다.");
    }

    @PostMapping("/create/room/single")
    public String createSingleRoom() {
        String roomId = roomService.createRoom(rq.getMember().getId(),rq.getMember().getUsername(),RoomStatus.SINGLE);

        return rq.redirectWithMsg("/chat/room/%s".formatted(roomId),"채팅 방이 생성되었습니다.");
    }

    @GetMapping("/room/{roomId}")
    public String entranceRoom(Model model, @PathVariable String roomId) {

        ChatRoom room = roomService.findBySecretId(roomId);
        model.addAttribute("room",room);

        return "chat/room";
    }

    @DeleteMapping("/room/delete")
    public ResponseEntity<String> deleteRoom(DeleteRoomDto deleteRoomDto) {

        Long memberId = rq.getMember().getId();

        if (!isRoomOwner(deleteRoomDto, memberId)) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        roomService.deleteRoom(deleteRoomDto.getRoomId());
        return new ResponseEntity<>(HttpStatus.OK);
    }

    private boolean isRoomOwner(DeleteRoomDto deleteRoomDto, Long memberId) {
        return deleteRoomDto.getUserId().equals(memberId);
    }


}
