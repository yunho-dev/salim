package com.salim.global.advice;

import com.salim.member.dto.MemberResponse;
import com.salim.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

@ControllerAdvice(annotations = Controller.class) // @RestController(API)엔 안 타게 스코프 한정
@RequiredArgsConstructor
public class GlobalControllerAdvice {

    private final MemberService memberService;

    @ModelAttribute("loginMember")
    public MemberResponse addLoginMember(@AuthenticationPrincipal String memberId) {
        if (memberId == null) {
            return null; // 로그인/회원가입 페이지 등 비로그인 상태 방어
        }
        return memberService.findLoginMember(memberId);
    }

}
