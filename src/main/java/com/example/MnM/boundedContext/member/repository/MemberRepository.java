package com.example.MnM.boundedContext.member.repository;

import com.example.MnM.boundedContext.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findByUserId(String username);

    Optional<Member> findByUsername(String username);

    List<Member> findByMbtiName(String mbtiName);


}