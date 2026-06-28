package com.salim.global.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Component
public class JwtProvider {
    private final SecretKey secretKey;
    private final long expiration;

    public JwtProvider(
            @Value("${jwt.secret}") String secret,
            @Value("${jwt.expiration}") long expiration) {
        this.secretKey = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
        this.expiration = expiration;
    }

    // 토큰 생성
    public String generateToken(String memberId) {
        Date now = new Date();
        return Jwts.builder()
                .subject(memberId) // 사용자 id
                .issuedAt(now) // 발급시간
                .expiration(new Date(now.getTime() + expiration)) // 만료시간
                .signWith(secretKey) // 서명
                .compact(); // 직렬화
    }

    // 토큰 유효성 검증
    public boolean validateToken(String token) {
        try {
            Jwts.parser()
                    .verifyWith(secretKey)
                    .build()
                    .parseSignedClaims(token); // 서명, 토큰형식, 만료시간 검증
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }

    // 토큰에서 memberId 추출, 필요시 메일 추출 메서드 등도 추가하면 됨
    public String getMemberId(String token) {
        return getClaims(token).getSubject();
    }

    // Claims 파싱 (내부 공통 메서드)
    private Claims getClaims(String token) {
        return Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

}
