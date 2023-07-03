package com.example.MnM.boundedContext.chat.controller;

import com.example.MnM.boundedContext.room.entity.ChatRoom;
import com.example.MnM.boundedContext.room.repository.RoomRepository;
import com.example.MnM.boundedContext.member.entity.Member;
import com.example.MnM.boundedContext.member.repository.MemberRepository;
import com.example.MnM.boundedContext.room.controller.RoomController;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
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
    MemberRepository memberRepository;

    @Autowired
    MockMvc mvc;

    @DisplayName("채팅 방 목록")
    @WithUserDetails("user3")
    @Test
    void showRoomList() throws Exception {
        mvc.perform(get("/chat/rooms"))
                .andExpect(handler().handlerType(RoomController.class))
                .andExpect(handler().methodName("roomList"))
                .andExpect(view().name("chat/rooms"))
                .andExpect(status().isOk());
    }


    @DisplayName("채팅 방 생성")
    @WithUserDetails("user3")
    @Test
    void createRoom() throws Exception {
        mvc.perform(post("/chat/create/room/group")
                        .with(csrf()))
                .andExpect(handler().handlerType(RoomController.class))
                .andExpect(handler().methodName("createGroupRoom"))
                .andExpect(redirectedUrlPattern("/chat/room/**"));

        List<ChatRoom> rooms = roomRepository.findAll();
        assertThat(rooms).hasSize(1);
    }

    @DisplayName("채팅 방 입장")
    @WithUserDetails("user3")
    @Test
    void enterRoom() throws Exception {

        ChatRoom room = ChatRoom.builder().secretId("uniqueId").build();
        roomRepository.save(room);

        mvc.perform(get("/chat/room/%s".formatted(room.getSecretId())))
                .andExpect(handler().handlerType(RoomController.class))
                .andExpect(handler().methodName("entranceRoom"))
                .andExpect(model().attributeExists("room"))
                .andExpect(view().name("chat/room"))
                .andExpect(status().is2xxSuccessful());
    }


}