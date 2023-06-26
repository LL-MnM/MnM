package com.example.MnM.boundedContext.board.answer;


import com.example.MnM.boundedContext.board.question.Question;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class AnswerService {
    private final AnswerRepository answerRepository;
    public Answer create(Question question, String content) {
        Answer answer = new Answer(content , LocalDateTime.now() , question);
        answerRepository.save(answer);

        return answer;
    }
}
