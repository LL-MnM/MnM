package com.example.MnM.boundedContext.recommend.service;

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

    public List<Member> findMbtiForMember(Member member) {
        Mbti mbti = mbtiService.findByName(member.getMbtiName());
        String name = mbti.getBestMbti();
        List<Member> members = memberService.findByMbtiName(name);
        return members;
    }
}
