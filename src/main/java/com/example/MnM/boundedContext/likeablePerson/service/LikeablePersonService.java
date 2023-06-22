package com.example.MnM.boundedContext.likeablePerson.service;

import com.example.MnM.base.rsData.RsData;
import com.example.MnM.boundedContext.likeablePerson.entity.LikeablePerson;
import com.example.MnM.boundedContext.likeablePerson.repository.LikeablePersonRepository;
import com.example.MnM.boundedContext.member.entity.Member;
import com.example.MnM.boundedContext.member.repository.MemberRepository;
import com.example.MnM.boundedContext.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class LikeablePersonService {

    private final LikeablePersonRepository likeablePersonRepository;
    private final MemberService memberService;

    private final MemberRepository memberRepository;

    public Optional<LikeablePerson> findById(Long id) {
        return likeablePersonRepository.findById(id);
    }

    @Transactional
    public RsData<LikeablePerson> like(Member member, String username) {
        Optional<Member> optionalMember = memberService.findByUserName(username);
        if (optionalMember.isEmpty()) {
            return RsData.of("F-1", "존재하지 않는 유저 입니다.");
        }

        if (member.getUsername().equals(username)) {
            return RsData.of("F-2", "본인을 호감상대로 등록할 수 없습니다.");
        }
        LikeablePerson likeablePerson = LikeablePerson
                .builder()
                .fromMember(member)
                .toMember(optionalMember.get())
                .build();

        likeablePersonRepository.save(likeablePerson); // 저장

        // 너가 좋아하는 호감표시 생겼어.
        member.addFromLikeablePerson(likeablePerson);

        // 너를 좋아하는 호감표시 생겼어.
        Optional<Member> memberOptional = memberRepository.findByUsername(username);
        memberOptional.get().addToLikeablePerson(likeablePerson);

        return RsData.of("S-1", "호감을 표시하셨습니다.", likeablePerson);
    }

    public RsData<LikeablePerson> cancel(LikeablePerson likeablePerson) {
        likeablePersonRepository.delete(likeablePerson); // 저장

        return RsData.of("S-1", "호감을 표시하셨습니다.", likeablePerson);
    }
}
