package com.example.MnM.boundedContext.mbtiboard.service;

import com.example.MnM.boundedContext.mbtiboard.entity.mbtianswer.MbtiAnswer;
import com.example.MnM.boundedContext.mbtiboard.entity.mbtianswer.MbtiVote;
import com.example.MnM.boundedContext.mbtiboard.entity.mbtiquestion.MbtiDataNotFoundException;
import com.example.MnM.boundedContext.mbtiboard.entity.mbtiquestion.MbtiQuestion;
import com.example.MnM.boundedContext.mbtiboard.repository.MbtiAnswerRepository;
import com.example.MnM.boundedContext.member.entity.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@Transactional
@Service
@RequiredArgsConstructor
public class MbtiAnswerService {
    private final MbtiAnswerRepository mbtiAnswerRepository; // 변수명 수정

    public MbtiAnswer create(MbtiQuestion question, String content, Member member) {
        MbtiAnswer answer = new MbtiAnswer(content , LocalDateTime.now() , question, member);
        mbtiAnswerRepository.save(answer); // 변수명 수정

        return answer;
    }

    public MbtiAnswer getAnswer(Integer id) { // 타입 수정
        Optional<MbtiAnswer> answer = mbtiAnswerRepository.findById(id); // 변수명 수정
        if (answer.isEmpty())
            throw new MbtiDataNotFoundException("answer not found");
        return answer.get();
    }

    public void modify(MbtiAnswer answer, String content) { // 타입 수정
        answer.updateAnswer(content);
    }

    public void delete(MbtiAnswer answer) { // 타입 수정
        mbtiAnswerRepository.delete(answer); // 변수명 수정
    }

    public void vote(MbtiAnswer answer, Member voter) { // 타입 수정
        // 처음에 중복 투표를 확인한다.
        Optional<MbtiVote> existingVote = answer.getVotes().stream()
                .filter(vote -> vote.getMember().getNickname().equals(voter.getNickname())) // 이 부분은 회원의 고유 식별자에 따라 다를 수 있음
                .findFirst();

        if (existingVote.isPresent()) {
            throw new IllegalStateException("User already voted for this answer: " + voter.getNickname());
        }
        answer.addVoter(voter);
    }
}
