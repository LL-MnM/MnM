package com.example.MnM.boundedContext.room.controller;

import com.example.MnM.base.rq.Rq;
import com.example.MnM.boundedContext.member.entity.Member;
import com.example.MnM.boundedContext.room.dto.EnterRoomDto;
import com.example.MnM.boundedContext.room.entity.ChatRoom;
import com.example.MnM.boundedContext.room.entity.RoomStatus;
import com.example.MnM.boundedContext.room.service.RoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

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
        String roomId = roomService.createRoom(rq.getMember().getUsername(), RoomStatus.GROUP);

        return rq.redirectWithMsg("/chat/room/%s".formatted(roomId),"채팅 방이 생성되었습니다.");
    }

    @PostMapping("/create/room/single")
    public String createSingleRoom() {
        String roomId = roomService.createRoom(rq.getMember().getUsername(),RoomStatus.SINGLE);

        return rq.redirectWithMsg("/chat/room/%s".formatted(roomId),"채팅 방이 생성되었습니다.");
    }

    @GetMapping("/room/{roomId}")
    public String entranceRoom(Model model, @PathVariable String roomId) {

        ChatRoom room = roomService.findBySecretId(roomId);

        Member member = rq.getMember();
        EnterRoomDto enterRoomDto = new EnterRoomDto(member.getUsername(), member.getId());

        model.addAttribute("room", room);
        model.addAttribute("enterPerson", enterRoomDto);


        return "chat/room";
    }

}
