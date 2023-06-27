package com.example.MnM.boundedContext.board.service;


import com.example.MnM.boundedContext.board.entity.answer.Answer;
import com.example.MnM.boundedContext.board.entity.question.Question;
import com.example.MnM.boundedContext.board.repository.AnswerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Transactional
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
