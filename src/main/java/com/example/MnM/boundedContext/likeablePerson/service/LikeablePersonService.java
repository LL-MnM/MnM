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
    public RsData<LikeablePerson> like(Member member, String name) {
        Optional<Member> optionalMember = memberService.findByName(name);
        if (optionalMember.isEmpty()) {
            return RsData.of("F-1", "존재하지 않는 유저 입니다.");
        }

        if (member.getName().equals(name)) {
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
        Optional<Member> memberOptional = memberRepository.findByName(name);
        memberOptional.get().addToLikeablePerson(likeablePerson);

        return RsData.of("S-1", "호감을 표시하셨습니다.", likeablePerson);
    }

    @Transactional
    public RsData<LikeablePerson> cancel(LikeablePerson likeablePerson, Long memberId) {
        if (!likeablePerson.getFromMember().getId().equals(memberId)) {
            return RsData.of("F-1", "삭제할 권한이 없습니다.");
        }
        likeablePersonRepository.delete(likeablePerson);

        return RsData.of("S-1", "호감을 취소하셨습니다.", likeablePerson);
    }

    @Transactional
    public RsData<LikeablePerson> modify(Member member, Long memberId, String name) {
        if (!member.getId().equals(memberId)) {
            return RsData.of("F-1", "수정할 권한이 없습니다.");
        }
        Optional<LikeablePerson> likeablePersonOptional = likeablePersonRepository.findById(memberId);
        if (likeablePersonOptional.isEmpty()) {
            return RsData.of("F-2", "멤버가 존재하지 않습니다.");
        }

        Optional<Member> memberOptional = memberRepository.findByName(name);
        if (memberOptional.isEmpty()) {
            return RsData.of("F-3", "호감을 수정할 멤버가 존재하지 않습니다.");
        }
        LikeablePerson likeablePerson = likeablePersonOptional.get();
        likeablePerson.getToMember().changeUsername(name);
        return RsData.of("S-1", "호감상대를 변경하셨습니다.", likeablePerson);
    }
}
