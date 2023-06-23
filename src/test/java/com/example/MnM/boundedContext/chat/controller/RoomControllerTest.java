package com.example.MnM.boundedContext.chat.controller;

import com.example.MnM.boundedContext.chat.entity.ChatRoom;
import com.example.MnM.boundedContext.chat.repository.RoomRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@Transactional
@AutoConfigureMockMvc
@ActiveProfiles("test")
@SpringBootTest
class RoomControllerTest {

    @Autowired
    RoomRepository roomRepository;

    @Autowired
    RoomController roomController;

    @Autowired
    MockMvc mvc;

    @DisplayName("채팅 방 목록")
    @WithUserDetails("user1")
    @Test
    void showRoomList() throws Exception {
        mvc.perform(get("/chat/rooms"))
                .andExpect(handler().handlerType(RoomController.class))
                .andExpect(handler().methodName("roomList"))
                .andExpect(view().name("chat/rooms"))
                .andExpect(status().isOk());
    }


    @DisplayName("채팅 방 생성")
    @WithUserDetails("user1")
    @Test
    void createRoom() throws Exception {
        mvc.perform(post("/chat/create/room")
                        .with(csrf()))
                .andExpect(handler().handlerType(RoomController.class))
                .andExpect(handler().methodName("createRoom"))
                .andExpect(redirectedUrlPattern("/chat/rooms**"));

        List<ChatRoom> rooms = roomRepository.findAll();
        assertThat(rooms).hasSize(1);
    }

    @DisplayName("채팅 방 입장")
    @WithUserDetails("user1")
    @Test
    void enterRoom() throws Exception {

        ChatRoom room = ChatRoom.builder().build();
        roomRepository.save(room);

        mvc.perform(get("/chat/room/%s".formatted(room.getId())))
                .andExpect(handler().handlerType(RoomController.class))
                .andExpect(handler().methodName("entranceRoom"))
                .andExpect(model().attributeExists("room"))
                .andExpect(view().name("chat/room"))
                .andExpect(status().is2xxSuccessful());
    }



}