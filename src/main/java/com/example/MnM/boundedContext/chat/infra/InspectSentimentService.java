package com.example.MnM.boundedContext.chat.infra;

import com.example.MnM.boundedContext.chat.dto.SentimentDto;

import java.io.IOException;
import java.util.concurrent.CompletableFuture;

public interface InspectSentimentService {

    CompletableFuture<SentimentDto> chatInspectSentiment(String msg) throws IOException;
}
