package com.example.MnM.boundedContext.room.controller;

import com.example.MnM.base.appConfig.AppConfig;
import com.example.MnM.base.rq.Rq;
import com.example.MnM.boundedContext.member.entity.Member;
import com.example.MnM.boundedContext.room.dto.EnterRoomDto;
import com.example.MnM.boundedContext.room.entity.ChatRoom;
import com.example.MnM.boundedContext.room.service.RoomService;
import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@PreAuthorize("isAuthenticated()")
@RequiredArgsConstructor
@RequestMapping("/chat")
@Controller
public class RoomController {

    private final RoomService roomService;
    private final Rq rq;

    @GetMapping("/rooms")
    public String roomList(Model model, @RequestParam(value="page", defaultValue="1") int page) {
        Page<ChatRoom> paging = this.roomService.getList(page);
        model.addAttribute("paging", paging);

        return "chat/rooms";
    }

    @PostMapping("/create/room/group")
    public String createGroupRoom() {
        String roomId = roomService.createGroupRoom(rq.getMember().getUsername());

        return rq.redirectWithMsg("/chat/room/%s".formatted(roomId), "채팅 방이 생성되었습니다.");
    }

    @PostMapping("/create/room/single")
    public String createSingleRoom(String username) {
        String roomId = roomService.createSingleRoom(rq.getMember().getUsername(), username);

        return rq.redirectWithMsg("/chat/room/%s".formatted(roomId), "채팅 방이 생성되었습니다.");
    }

    @GetMapping("/room/{roomId}")
    public String entranceRoom(Model model, @PathVariable String roomId) {


        Member member = rq.getMember();
        ChatRoom room = roomService.checkValidate(roomId, member.getUsername());
        EnterRoomDto enterRoomDto = new EnterRoomDto(member.getUsername(), member.getId());

        model.addAttribute("url", AppConfig.getChatUrl());
        model.addAttribute("room", room);
        model.addAttribute("enterPerson", enterRoomDto);

        return "chat/room";
    }

}
