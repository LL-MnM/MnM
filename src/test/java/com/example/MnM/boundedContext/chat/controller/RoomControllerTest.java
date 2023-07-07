package com.example.MnM.boundedContext.chat.controller;

import com.example.MnM.boundedContext.member.repository.MemberRepository;
import com.example.MnM.boundedContext.room.controller.RoomController;
import com.example.MnM.boundedContext.room.entity.ChatRoom;
import com.example.MnM.boundedContext.room.repository.RoomRepository;
import com.example.MnM.boundedContext.room.service.RoomService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@AutoConfigureMockMvc
@ActiveProfiles("test")
@SpringBootTest
class RoomControllerTest {

    @Autowired
    RoomRepository roomRepository;

    @Autowired
    RoomService roomService;

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    MockMvc mvc;

    @Autowired
    RedisTemplate<String,Object> redisTemplate;

    @AfterEach
    void deleteRedisValue() {
        Set<String> keys = redisTemplate.keys("*");
        if (keys != null)
            keys.forEach(k-> {
                redisTemplate.delete(k);
            });
    }


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


    @DisplayName("그룹 채팅 방 생성")
    @WithUserDetails("user3")
    @Test
    void createGroupRoom() throws Exception {
        MvcResult mvcResult = mvc.perform(post("/chat/create/room/group")
                        .with(csrf()))
                .andExpect(handler().handlerType(RoomController.class))
                .andExpect(handler().methodName("createGroupRoom"))
                .andExpect(redirectedUrlPattern("/chat/room/**"))
                .andReturn();

        String url = mvcResult.getResponse().getRedirectedUrl();
        String uuid = url.substring(url.lastIndexOf("/") + 1, url.indexOf("?"));

        Optional<ChatRoom> roomOptional = roomRepository.findBySecretId(uuid);
        assertThat(roomOptional).isNotEmpty();
        assertThat(roomOptional.get().isGroup()).isTrue();
    }

    @DisplayName("개인 채팅 방 생성")
    @WithUserDetails("user3")
    @Test
    void createSingleRoom() throws Exception {
        MvcResult mvcResult = mvc.perform(post("/chat/create/room/single")
                        .with(csrf()))
                .andExpect(handler().handlerType(RoomController.class))
                .andExpect(handler().methodName("createSingleRoom"))
                .andExpect(redirectedUrlPattern("/chat/room/**"))
                .andReturn();

        String url = mvcResult.getResponse().getRedirectedUrl();
        String uuid = url.substring(url.lastIndexOf("/") + 1, url.indexOf("?"));

        Optional<ChatRoom> roomOptional = roomRepository.findBySecretId(uuid);
        assertThat(roomOptional).isNotEmpty();
        assertThat(roomOptional.get().isGroup()).isFalse();
    }

    @DisplayName("그룹 채팅 방 입장")
    @WithUserDetails("user3")
    @Test
    void enterGroupRoom() throws Exception {

        String secretId = roomService.createGroupRoom("user3");

        mvc.perform(get("/chat/room/%s".formatted(secretId)))
                .andExpect(handler().handlerType(RoomController.class))
                .andExpect(handler().methodName("entranceRoom"))
                .andExpect(model().attributeExists("room"))
                .andExpect(view().name("chat/room"))
                .andExpect(status().is2xxSuccessful());
    }

    @DisplayName("개인 채팅 방 입장")
    @WithUserDetails("user3")
    @Test
    void enterSingleRoom() throws Exception {

        String secretId = roomService.createSingleRoom("user3", "user4");

        mvc.perform(get("/chat/room/%s".formatted(secretId)))
                .andExpect(handler().handlerType(RoomController.class))
                .andExpect(handler().methodName("entranceRoom"))
                .andExpect(model().attributeExists("room"))
                .andExpect(view().name("chat/room"))
                .andExpect(status().is2xxSuccessful());

    }


}