package com.example.MnM.boundedContext.chat.infra;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;


@Setter
@Getter
@NoArgsConstructor
public class SentimentResult {
    private List<Sentences> sentences;

    private String language;

    private DocumentSentiment documentSentiment;

    @Getter
    @NoArgsConstructor
    public static class Sentences {

        private Sentiment sentiment;

        private Text text;

        public Sentiment getSentiment() {
            return sentiment;
        }

    }

    @Getter
    @NoArgsConstructor
    public static class Sentiment {

        private double score;

        private double magnitude;


    }

    @Getter
    @NoArgsConstructor
    public static class Text {

        private int beginoffset;

        private String content;

    }

    @Getter
    @NoArgsConstructor
    public static class DocumentSentiment {

        private float score;

        private float magnitude;

    }
}
