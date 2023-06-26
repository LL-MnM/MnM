package com.example.MnM.Board;

import com.example.MnM.boundedContext.board.repository.AnswerRepository;
import com.example.MnM.boundedContext.board.entity.question.Question;
import com.example.MnM.boundedContext.board.repository.QuestionRepository;
import com.example.MnM.boundedContext.board.service.QuestionService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
class BoardApplicationTests {

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private AnswerRepository answerRepository;

    @Autowired
    private QuestionService questionService;

    @Test
    void test_CreateQuestions_Setup() {
        for (int i = 1; i <= 100; i++) {
            String subject = String.format("Sample Question %d", i);
            String content = "Sample Question Content";
            this.questionService.create(subject, content);
        }
        assertEquals(100, questionRepository.count());
    }

    @Test
    void test_FindAllQuestions() {
        List<Question> allQuestions = questionRepository.findAll();
        assertEquals(100, allQuestions.size());
    }

    @Test
    void test_FindById() {
        Optional<Question> oq = questionRepository.findById(99);
        assertTrue(oq.isPresent());

        Question q = oq.get();
        assertEquals("Sample Question 99", q.getSubject());
    }

    @Test
    void test_FindBySubject() {
        String subject = "Sample Question 99";
        Question q = questionRepository.findBySubject(subject);
        assertEquals(99, q.getId());
    }

    @Test
    void test_FindBySubjectAndContent() {
        String subject = "Sample Question 99";
        String content = "Sample Question Content";
        Question q = questionRepository.findBySubjectAndContent(subject, content);
        assertEquals(99, q.getId());
    }

    @Test
    void test_FindBySubjectLike() {
        List<Question> qList = questionRepository.findBySubjectLike("Sample%");
        assertEquals(100, qList.size());
    }

    @Test
    void test_UpdateQuestion() {
        Optional<Question> oq = questionRepository.findById(99);
        assertTrue(oq.isPresent());

        Question q = oq.get();
        q.createQuestion("Updated Subject", q.getContent());
        questionRepository.save(q);
    }
}
