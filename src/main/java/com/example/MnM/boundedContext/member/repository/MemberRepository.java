package com.example.MnM.boundedContext.member.repository;

import com.example.MnM.boundedContext.member.entity.Member;
import jakarta.persistence.Entity;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findByUsername(String username); //아이디로 찾기

    Optional<Member> findByName(String name); //

    Optional<Member> findByUsernameAndEmail(String username, String email);

    Optional<Member> findByMbti(String mbti);

}