package com.example.MnM.boundedContext.chat.infra;

import com.example.MnM.boundedContext.chat.entity.EmotionDegree;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class InspectSentimentServiceTest {

    @Autowired
    InspectSentimentService inspectSentimentService;

    @Test
    void inspectTest() throws IOException, ExecutionException, InterruptedException {
        String tendency ="ISFJ";
        String msg = "hello world";
        CompletableFuture<EmotionDegree> sentiment = inspectSentimentService.chatInspectSentiment(tendency, msg);

        EmotionDegree emotionDegree = sentiment.get();

        assertThat(emotionDegree.getMagnitude()).isInstanceOf(Float.class);
        assertThat(emotionDegree.getScore()).isInstanceOf(Float.class);
        assertThat(emotionDegree.getTendency()).isEqualTo(tendency);
    }

}