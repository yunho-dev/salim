package com.salim.member.service;

import com.salim.global.jwt.JwtProvider;
import com.salim.member.entity.Member;
import com.salim.member.repository.MemberRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final MemberRepository memberRepository;  // JPA는 DAO 대신 Repository
    private final PasswordEncoder passwordEncoder;
    private final JwtProvider jwtProvider;

    private final Logger logger = LoggerFactory.getLogger(getClass());

    // 로그인 처리
    public String login(String memberId, String password) {

        logger.info("로그인 시도(서비스 진입) - memberId: {}", memberId);

        // 1. DB에서 회원 조회
        Member member = memberRepository.findByMemberId(memberId)
                .orElseThrow(() -> {
                    logger.warn("존재하지 않는 회원 - memberId: {}", memberId);
                    return new IllegalArgumentException("존재하지 않는 회원입니다.");
                });

        // 2. 비밀번호 검증
        if (!passwordEncoder.matches(password, member.getPassword())) {
            logger.warn("비밀번호 불일치 - memberId: {}", memberId);
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }

        // 3. 토큰 발급
        logger.info("로그인 토큰 발급 성공 - memberId: {}", memberId);
        return jwtProvider.generateToken(memberId);
    }
}
