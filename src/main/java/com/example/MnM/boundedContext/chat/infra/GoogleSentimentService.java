package com.example.MnM.boundedContext.chat.infra;

import com.example.MnM.boundedContext.chat.dto.SentimentDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.http.*;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

import static com.example.MnM.boundedContext.chat.infra.SentimentResult.DocumentSentiment;

@Slf4j
@Profile("prod")
@Component
public class GoogleSentimentService implements InspectSentimentService {

    @Value("${secret.google.place}")
    private String keyPath;

    @Value("${secret.google.apiKey}")
    private String apiKey;


    @Async("googleService")
    public CompletableFuture<SentimentDto> chatInspectSentiment(String msg) throws IOException {


        RestTemplate restTemplate = new RestTemplate();
        String url = "https://language.googleapis.com/v1/documents:analyzeSentiment?key=%s".formatted(apiKey);


        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("encodingType", "UTF8");
        Map<String, Object> document = new HashMap<>();
        document.put("type", "PLAIN_TEXT");
        document.put("content", msg);
        requestBody.put("document", document);

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestBody, headers);

        ResponseEntity<SentimentResult> response = restTemplate.exchange(url, HttpMethod.POST, entity, SentimentResult.class);

        SentimentResult sentimentResult = response.getBody();

        DocumentSentiment sentiment = sentimentResult.getDocumentSentiment();

        return CompletableFuture.completedFuture(new SentimentDto(sentiment.getMagnitude(), sentiment.getScore()));
    }
}

