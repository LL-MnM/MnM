package com.example.MnM.boundedContext.chat.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;


@Slf4j
@RequiredArgsConstructor
@Component
public class ChatSchedulerService {

    private final RedisTemplate<String,Object> redisTemplate;

    @Async
    @Scheduled(cron = "0 0 * * * *")
    public void persistsChat() {
    }

}
