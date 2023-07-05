package com.example.MnM.boundedContext.chat.infra;

import com.example.MnM.boundedContext.chat.dto.SentimentDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.util.Random;
import java.util.concurrent.CompletableFuture;

@Slf4j
@Component
@Profile({"dev", "test"})
public class LocalInspectService implements InspectSentimentService {
    @Override
    public CompletableFuture<SentimentDto> chatInspectSentiment(String msg) {

        Random random = new Random();
        log.info("LocalInspectService 호출 = {}", msg);
        return CompletableFuture.completedFuture(new SentimentDto(random.nextFloat(), random.nextFloat()));
    }
}
