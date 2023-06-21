package com.example.MnM.boundedContext.chat.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RequiredArgsConstructor
@RequestMapping("/chat")
@Controller
public class RoomController {

    @GetMapping("/rooms")
    public String rooms(Model model) {
        return "chat/rooms";
    }

}
