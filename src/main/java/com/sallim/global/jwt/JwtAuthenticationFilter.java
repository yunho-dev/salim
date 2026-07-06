package com.sallim.global.jwt;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter { // 요청당 딱 한 번만 실행하는 것을 보장하기 위해 OncePerRequestFilter 상속(서블릿 필터는 원래 여러번 실행될 수 있음)

    private final JwtProvider jwtProvider;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {

        String token = extractTokenFromCookie(request); // 쿠키에서 토큰 추출

        if (token != null && jwtProvider.validateToken(token)) { // 토큰이 있고, 검증 통과 시
            String memberId = jwtProvider.getMemberId(token);

            // 사용자 인증 정보 생성
            UsernamePasswordAuthenticationToken authentication =
                    new UsernamePasswordAuthenticationToken(memberId, null, Collections.emptyList()); // 검증이 끝났기에 null, 권한 시스템이 없기에 empty

            SecurityContextHolder.getContext().setAuthentication(authentication); // 스프링 시큐리티에 인증된 사용자 등록
        }

        // 토큰이 없거나 검증 실패해도 필터는 그대로 실행.
        // 이 필터는 그냥 "토큰 있으면 신원 확인해서 등록한다"에 대한 역할을 함
        // 한 마디로 인증 정보를 채워주는 역할이고, 실제로 막는 로직은 SecurityConfig의 authorizeHttpRequests가 함
        // 이렇게 역할 분리 하는 것이 스프링 시큐리티의 필터 설계 패턴
        filterChain.doFilter(request, response); // 다음 필터로 요청 보냄
    }

    private String extractTokenFromCookie(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies(); // 쿠키가 없으면 null을 반환함
        if (cookies == null) return null;

        for (Cookie cookie : cookies) {
            if ("token".equals(cookie.getName())) {
                return cookie.getValue();
            }
        }
        return null;
    }
}