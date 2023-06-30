package com.example.MnM.boundedContext.chat.infra;

import com.example.MnM.boundedContext.chat.dto.SentimentDto;
import com.google.api.gax.core.FixedCredentialsProvider;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.language.v1.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.concurrent.CompletableFuture;

@Slf4j
@Profile("prod")
@Component
public class GoogleSentimentService implements InspectSentimentService {

    @Value("${secret.google.place}")
    private String keyPath;

    @Async("googleService")
    public CompletableFuture<SentimentDto> chatInspectSentiment(String msg) throws IOException {

        GoogleCredentials credentials = GoogleCredentials.fromStream(new FileInputStream(keyPath));

        try (LanguageServiceClient language = LanguageServiceClient.create(LanguageServiceSettings.newBuilder()
                .setCredentialsProvider(FixedCredentialsProvider.create(credentials))
                .build())) {

            Document doc = Document.newBuilder().setContent(msg).setType(Document.Type.PLAIN_TEXT).build();
            AnalyzeSentimentResponse response = language.analyzeSentiment(doc);
            Sentiment sentiment = response.getDocumentSentiment();

            if (sentiment == null) {
                log.debug("No sentiment found");
            }

            return CompletableFuture.completedFuture(new SentimentDto(sentiment.getMagnitude(), sentiment.getScore()));
        }
    }
}
