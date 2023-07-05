package com.example.MnM.boundedContext.member.entity;

import com.example.MnM.base.baseEntity.BaseEntity;

import com.example.MnM.boundedContext.likeablePerson.entity.LikeablePerson;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OrderBy;
import jakarta.validation.constraints.Email;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;
import org.hibernate.annotations.SQLDelete;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;


import java.util.ArrayList;
import java.util.List;


@Entity
@SuperBuilder(toBuilder = true)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@SQLDelete(sql = "UPDATE member SET deleted = true WHERE id = ?")
public class Member extends BaseEntity {

    private String username; //id
    private String password; //pw
    private String name; //이름
    @Email
    private String email; //이메일
    private Boolean emailVerified; //이메일 인증 확인
    private String providerType; //소셜로그인을 위한 제공자 타입

    private boolean deleted = Boolean.FALSE; //soft delete
    //Todo : dateTime으로 교체 ->sql문 수정해야함

    //여기까지 회원기본정보, 아래로는 개인정보

    private String nickname; //닉네임
    private Integer height; //키
    private Integer age; //나이;
    private String locate; //지역
    private String gender; //성별
    private String mbti; //mbti
    private String hobby; //취미
    private String profileImage; //프로필사진, 임시로 만듬
    private String introduce; //자기소개



    public Member(String username, String password, String providerType) {
        this.username = username;
        this.password =password;
        this.providerType = providerType;

    }

    public List<? extends GrantedAuthority> getGrantedAuthorities() { //시큐리티에 등록된 권한
        List<GrantedAuthority> grantedAuthorities = new ArrayList<>();

        grantedAuthorities.add(new SimpleGrantedAuthority("USER"));

        if ("admin".equals(username)) {
            grantedAuthorities.add(new SimpleGrantedAuthority("ADMIN"));
        }

        return grantedAuthorities;
    }


    @OneToMany(mappedBy = "fromMember", cascade = {CascadeType.ALL})
    @OrderBy("id desc") // 정렬
    @LazyCollection(LazyCollectionOption.EXTRA)
    @Builder.Default // @Builder 가 있으면 ` = new ArrayList<>();` 가 작동하지 않는다. 그래서 이걸 붙여야 한다.
    private List<LikeablePerson> fromLikeablePeople = new ArrayList<>();

    @OneToMany(mappedBy = "toMember", cascade = {CascadeType.ALL})
    @OrderBy("id desc") // 정렬
    @LazyCollection(LazyCollectionOption.EXTRA)
    @Builder.Default // @Builder 가 있으면 ` = new ArrayList<>();` 가 작동하지 않는다. 그래서 이걸 붙여야 한다.
    private List<LikeablePerson> toLikeablePeople = new ArrayList<>();

    public void addFromLikeablePerson(LikeablePerson likeablePerson) {
        fromLikeablePeople.add(0, likeablePerson);
    }

    public void addToLikeablePerson(LikeablePerson likeablePerson) {
        toLikeablePeople.add(0, likeablePerson);
    }

    public void changeUsername(String name) {
        this.name = name;
    }
}