package com.example.MnM.boundedContext.member.repository;

import com.example.MnM.boundedContext.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findByUsername(String username); //아이디로 찾기

    Optional<Member> findByName(String name); //

    Optional<Member> findByUsernameAndEmail(String username, String email);

    List<Member> findByMbti(String mbti);


}