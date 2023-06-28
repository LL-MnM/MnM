package com.example.MnM.boundedContext.chat.infra;

import com.example.MnM.boundedContext.chat.entity.EmotionDegree;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.util.Random;
import java.util.concurrent.CompletableFuture;

@Slf4j
@Component
@Profile({"dev","test"})
public class LocalInspectService implements InspectSentimentService {
    @Override
    public CompletableFuture<EmotionDegree> chatInspectSentiment(String tendency, String msg) {

        Random random = new Random();
        log.info("LocalInspectService 호출 = {}",msg);
        return CompletableFuture.completedFuture(new EmotionDegree(tendency,random.nextFloat(), random.nextFloat()));
    }
}
