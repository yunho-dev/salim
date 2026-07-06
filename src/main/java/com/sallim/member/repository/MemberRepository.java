package com.sallim.member.repository;

import com.sallim.member.entity.Member;
import jakarta.validation.constraints.Pattern;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, String> {

    // member_id로 조회 (로그인용)
    Optional<Member> findByMemberId(String memberId);

    // member_id 중복 체크 (회원가입용)
    boolean existsByMemberId(String memberId);

    // email 중복 체크 (회원가입용)
    boolean existsByEmail(@Pattern(regexp = "^[a-z0-9]{4,20}$", message = "아이디는 영문 소문자와 숫자 4~20자입니다.") String s);
}