package com.example.MnM.boundedContext.member.repository;

import com.example.MnM.boundedContext.member.entity.Member;
import jakarta.persistence.Entity;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findByUsername(String username);
    Optional<Member> findByName(String name);

    Optional<Member> findByUsernameAndEmail(String username, String email);


/*    @EntityGraph(attributePaths = "roleSet")
    @Query("select m from Member m where m.id = :id")
    Optional<Member> getWithRoles(String mid);*/
}