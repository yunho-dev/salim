package com.sallim.member.service;

import com.sallim.member.dto.MemberResponse;
import com.sallim.member.dto.SignupRequest;
import com.sallim.member.entity.Member;
import com.sallim.member.repository.MemberRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final Logger logger = LoggerFactory.getLogger(getClass());

    // 회원가입 처리
    @Transactional
    public void signUp(SignupRequest request) {
        // 1. 중복 체크
        if (memberRepository.existsByMemberId(request.memberId())) { // 아이디 체크
            throw new IllegalArgumentException("이미 존재하는 아이디입니다.");
        }

        if (memberRepository.existsByEmail(request.email())) { // 이메일 체크
            throw new IllegalArgumentException("이미 등록된 이메일입니다.");
        }

        // 2. 비밀번호 암호화
        Member member = Member.builder()
                .memberId(request.memberId())
                .password(passwordEncoder.encode(request.password()))
                .email(request.email())
                .nickname(request.nickname())
                .build();

        // 3. DB 저장
        memberRepository.save(member);
    }

    // 아이디 중복 체크
    public boolean existsByMemberId(String memberId) {
        return memberRepository.existsByMemberId(memberId);
    }

    // 이메일 체크
    public boolean existsByEmail(String email) {
        return memberRepository.existsByEmail(email);
    }

    // 로그인 유저 정보 조회
    public MemberResponse findLoginMember(String memberId) {
        return memberRepository.findByMemberId(memberId)
                .map(MemberResponse::from)
                .orElse(null);
    }
}
