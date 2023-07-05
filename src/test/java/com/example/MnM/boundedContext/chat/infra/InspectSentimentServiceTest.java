package com.example.MnM.boundedContext.chat.infra;

import com.example.MnM.boundedContext.chat.dto.SentimentDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import static org.assertj.core.api.Assertions.assertThat;

@Transactional
@ActiveProfiles("test")
@SpringBootTest
class InspectSentimentServiceTest {

    @Autowired
    InspectSentimentService inspectSentimentService;

    @DisplayName("감정 분석 호출 테스트")
    @Test
    void inspectTest() throws IOException, ExecutionException, InterruptedException {

        String msg = "hello world";
        CompletableFuture<SentimentDto> sentiment = inspectSentimentService.chatInspectSentiment(msg);

        SentimentDto emotionDegree = sentiment.get();

        assertThat(emotionDegree.magnitude()).isInstanceOf(Float.class);
        assertThat(emotionDegree.score()).isInstanceOf(Float.class);
    }

}