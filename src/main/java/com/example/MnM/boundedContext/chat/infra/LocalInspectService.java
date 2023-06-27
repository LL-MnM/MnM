package com.example.MnM.boundedContext.chat.infra;

import com.example.MnM.boundedContext.chat.entity.EmotionDegree;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.util.Random;
import java.util.concurrent.CompletableFuture;

@Component
@Profile("dev")
public class LocalInspectService implements InspectSentimentService {
    @Override
    public CompletableFuture<EmotionDegree> chatInspectSentiment(String msg) {

        Random random = new Random();

        return CompletableFuture.completedFuture(new EmotionDegree(random.nextFloat(), random.nextFloat()));
    }
}
