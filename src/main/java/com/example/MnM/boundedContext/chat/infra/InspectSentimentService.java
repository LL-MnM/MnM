package com.example.MnM.boundedContext.chat.infra;

import com.example.MnM.boundedContext.chat.entity.EmotionDegree;

import java.io.IOException;
import java.util.concurrent.CompletableFuture;

public interface InspectSentimentService {

    CompletableFuture<EmotionDegree> chatInspectSentiment(String msg) throws IOException;
}
