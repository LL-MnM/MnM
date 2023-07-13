package com.example.MnM.boundedContext.mbtiboard.service;

import com.example.MnM.boundedContext.mbtiboard.entity.mbtianswer.MbtiAnswer;
import com.example.MnM.boundedContext.mbtiboard.entity.mbtianswer.MbtiVote;
import com.example.MnM.boundedContext.mbtiboard.entity.mbtiquestion.MbtiDataNotFoundException;
import com.example.MnM.boundedContext.mbtiboard.entity.mbtiquestion.MbtiQuestion;
import com.example.MnM.boundedContext.mbtiboard.repository.MbtiAnswerRepository;
import com.example.MnM.boundedContext.member.entity.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.Optional;

@Transactional
@Service
@RequiredArgsConstructor
public class MbtiAnswerService {
    private final MbtiAnswerRepository mbtiAnswerRepository;

    public MbtiAnswer create(MbtiQuestion question, String content, Member member) {
        MbtiAnswer answer = new MbtiAnswer(content , LocalDateTime.now() , question, member);
        mbtiAnswerRepository.save(answer);

        return answer;
    }

    public MbtiAnswer getAnswer(Integer id) {
        return mbtiAnswerRepository.findById(id)
                .orElseThrow(() -> new MbtiDataNotFoundException("answer not found"));
    }

    public void modify(MbtiAnswer answer, String content) {
        answer.updateAnswer(content);
    }

    public void delete(MbtiAnswer answer) {
        mbtiAnswerRepository.delete(answer);
    }

    public void vote(MbtiAnswer answer, Member voter) {
        Optional<MbtiVote> existingVote = answer.getVotes().stream()
                .filter(vote -> vote.getMember().getNickname().equals(voter.getNickname())) // 이 부분은 회원의 고유 식별자에 따라 다를 수 있음
                .findFirst();

        if (existingVote.isPresent()) {
            throw new IllegalStateException("User already voted for this answer: " + voter.getNickname());
        }
        answer.addVoter(voter);
    }
    public void checkAuthority(MbtiAnswer answer, String username) {
        if (!answer.getMember().getUsername().equals(username)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "수정권한이 없습니다.");
        }
    }

}
