package com.sallim.member.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
class MemberViewController {
    Logger logger = LoggerFactory.getLogger(getClass());

    // 회원가입 페이지
    @GetMapping({"/signup"})
    public String signUpPage() {
        // 이미 인증된 사용자면 대시보드로 바로 보냄
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.isAuthenticated()
                && !(auth instanceof AnonymousAuthenticationToken)) {
            return "redirect:/dashboard";
        }

        return "member/signup";
    }

}
