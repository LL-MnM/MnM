package com.example.MnM.boundedContext.board.service;

import com.example.MnM.boundedContext.board.entity.answer.Answer;
import com.example.MnM.boundedContext.board.entity.question.DataNotFoundException;
import com.example.MnM.boundedContext.board.entity.question.Question;
import com.example.MnM.boundedContext.board.repository.AnswerRepository;
import com.example.MnM.boundedContext.member.entity.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@Transactional
@Service
@RequiredArgsConstructor
public class AnswerService {
    private final AnswerRepository answerRepository;

    public Answer create(Question question, String content, Member member) {
        Answer answer = new Answer(content , LocalDateTime.now() , question, member);
        answerRepository.save(answer);

        return answer;
    }

    public Answer getAnswer(Integer Long) {
        Optional<Answer> answer = answerRepository.findById(Long);
        if (answer.isEmpty())
            throw new DataNotFoundException("answer not found");
        return answer.get();
    }

    public void modify(Answer answer, String content) {
        answer.updateAnswer(content);
    }
    public void delete(Answer answer) {
        answerRepository.delete(answer);
    }
    public void vote(Answer answer, Member voter) {
        answer.addVoter(voter);
    }
}
