package com.example.MnM.Board;

import com.example.MnM.boundedContext.board.answer.Answer;
import com.example.MnM.boundedContext.board.answer.AnswerRepository;
import com.example.MnM.boundedContext.board.answer.AnswerService;
import com.example.MnM.boundedContext.board.question.Question;
import com.example.MnM.boundedContext.board.question.QuestionRepository;
import com.example.MnM.boundedContext.board.question.QuestionService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
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
    void testJpa() {
        Question q1 = new Question();
        q1.setSubject("MnM가 무엇인가요?");
        q1.setContent("MnM에 대해서 알고 싶습니다.");
        q1.setCreateDate(LocalDateTime.now());
        this.questionRepository.save(q1);  // 첫번째 질문 저장

        Question q2 = new Question();
        q2.setSubject("스프링부트 모델 질문입니다.");
        q2.setContent("id는 자동으로 생성되나요?");
        q2.setCreateDate(LocalDateTime.now());
        this.questionRepository.save(q2);  // 두번째 질문 저장
    }

    // 데이터 조회
    @Test
    void testJpa1() {
        List<Question> all = this.questionRepository.findAll();
        assertEquals(2, all.size());

        Question q = all.get(0);
        assertEquals("MnM가 무엇인가요?", q.getSubject());
    }

    // findById
    @Test
    void testJpa2() {
        Optional<Question> oq = this.questionRepository.findById(1);
        if (oq.isPresent()) {
            Question q = oq.get();
            assertEquals("MnM가 무엇인가요?", q.getSubject());
        }

    }

    // findBySubject
    @Test
    void testJpa3() {
        Question q = this.questionRepository.findBySubject("MnM가 무엇인가요?");
        assertEquals(1, q.getId());
    }

    // findBySubjectAndContent
    @Test
    void testJpa4() {
        Question q = this.questionRepository.findBySubjectAndContent(
                "MnM가 무엇인가요?", "MnM에 대해서 알고 싶습니다.");
        assertEquals(1, q.getId());
    }

    // findBySubjectLike
    @Test
    void testJpa5() {
        List<Question> qList = this.questionRepository.findBySubjectLike("MnM%");
        Question q = qList.get(0);
        assertEquals("MnM가 무엇인가요?", q.getSubject());
    }

    // 데이터 수정
    @Test
    void testJpa6() {
        Optional<Question> oq = this.questionRepository.findById(1);
        assertTrue(oq.isPresent());
        Question q = oq.get();
        q.setSubject("수정된 제목");
        this.questionRepository.save(q);
    }

    // 데이터 삭제
    @Test
    void testJpa7() {
        assertEquals(2, this.questionRepository.count());
        Optional<Question> oq = this.questionRepository.findById(1);
        assertTrue(oq.isPresent());
        Question q = oq.get();
        this.questionRepository.delete(q);
        assertEquals(1, this.questionRepository.count());
    }

    // 답변 데이터 생성 후 저장하기
    @Test
    void testJpa8() {
        Optional<Question> oq = this.questionRepository.findById(2);
        assertTrue(oq.isPresent());
        Question q = oq.get();

        Answer a = new Answer();
        a.setContent("네 자동으로 생성됩니다.");
        a.setQuestion(q);  // 어떤 질문의 답변인지 알기위해서 Question 객체가 필요하다.
        a.setCreateDate(LocalDateTime.now());
        this.answerRepository.save(a);
    }

    // 답변 조회하기
    @Test
    void testJpa9() {
        Optional<Answer> oa = this.answerRepository.findById(1);
        assertTrue(oa.isPresent());
        Answer a = oa.get();
        assertEquals(2, a.getQuestion().getId());
    }

    @Test
    void testJpa10() {
        for (int i = 1; i <= 100; i++) {
            String subject = String.format("테스트 데이터입니다:[%03d]", i);
            String content = "내용무";
            this.questionService.create(subject, content);
        }
    }
}

