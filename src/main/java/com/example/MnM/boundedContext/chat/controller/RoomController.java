package com.example.MnM.boundedContext.chat.controller;

import com.example.MnM.base.rq.Rq;
import com.example.MnM.boundedContext.chat.entity.ChatRoom;
import com.example.MnM.boundedContext.chat.service.RoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

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

    @PostMapping("/create/room")
    public String createRoom(Model model) {
//        Long roomId = roomService.createRoom(rq.getMember().getUsername());
        Long roomId = roomService.createRoom();
        model.addAttribute("roomId",roomId);
        return rq.redirectWithMsg("/chat/rooms","채팅 방이 생성되었습니다.");
    }

    @GetMapping("/room/{roomId}")
    public String entranceRoom(Model model, @PathVariable Long roomId) {

        ChatRoom room = roomService.findById(roomId);
        model.addAttribute("room",room);

        return "chat/room";
    }



}
