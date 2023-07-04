package com.example.MnM.boundedContext.recommend.service;

import com.example.MnM.boundedContext.chat.service.EmotionService;
import com.example.MnM.boundedContext.member.entity.Member;
import com.example.MnM.boundedContext.member.service.MemberService;
import com.example.MnM.boundedContext.recommend.entity.Mbti;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class MemberMbtiService {

    private final MbtiService mbtiService;
    private final MemberService memberService;
    private final EmotionService emotionService;

    public List<Member> findMbtiForMember(Member member) {

        if (emotionService.isExistsEmotionDegree(member.getId())) {
            String mbtiName = emotionService.findBestMbtiByDegree(member.getId());
            return memberService.findByMbti(mbtiName);
        }

        Mbti mbti = mbtiService.findByName(member.getMbti());
        List<Member> members = memberService.findByMbti(mbti.getBestMbti());
        return members;

    }
}
