package com.salim.member.controller;

import com.salim.member.service.AuthService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequiredArgsConstructor
//@RequestMapping("/")
class AuthController {
    Logger logger = LoggerFactory.getLogger(getClass());
    private final AuthService service;

    // TODO: 랜딩 페이지 추가되면 "/" 매핑 제거
    @GetMapping({"/", "/login"})
    public String loginPage() {
        // 이미 인증된 사용자면 대시보드로 바로 보냄
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.isAuthenticated()
                && !(auth instanceof AnonymousAuthenticationToken)) {
            return "redirect:/dashboard";
        }

        logger.info("로그인 페이지 접근");
        return "member/login";
    }

    // 로그인 처리
    @PostMapping("/api/members/login")
    public String login(String memberId, String password,
                        RedirectAttributes rAttr,
                        HttpServletResponse res) {
        logger.info("로그인 시도(컨트롤러 진입) - memberId: {}", memberId);

        if (memberId.isEmpty() || password.isEmpty()) {
            rAttr.addFlashAttribute("msg", "아이디/비밀번호를 입력해주세요.");
            logger.warn("아이디 혹은 비밀번호가 없습니다.");
            return "redirect:/";
        }

        try {
            String token = service.login(memberId, password);

            Cookie cookie = new Cookie("token", token);
            cookie.setHttpOnly(true);   // JS에서 접근 차단(JS에서 document.cookie로 이 쿠키에 접근 불가)
            cookie.setPath("/"); // 이 쿠키가 유효한 경로 범위
            res.addCookie(cookie);

            rAttr.addFlashAttribute("msg", "안녕하세요 " + memberId + "님");
            return "redirect:/dashboard";

        } catch (IllegalArgumentException e) {
            rAttr.addFlashAttribute("msg", e.getMessage());
            return "redirect:/";
        }
    }

    // 로그아웃
    @PostMapping("/logout")
    public String logout(HttpServletResponse res) {
        Cookie cookie = new Cookie("token", null);
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        cookie.setMaxAge(0); // 즉시 만료시켜서 브라우저가 쿠키 삭제하게 함
        res.addCookie(cookie);

        logger.info("로그아웃 처리");
        return "redirect:/login";
    }

}
