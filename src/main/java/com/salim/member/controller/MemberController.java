package com.salim.member.controller;

import com.salim.member.dto.SignupRequest;
import com.salim.member.service.MemberService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@Controller
//@RequestMapping("/")
@RequiredArgsConstructor // 생성자 주입
class MemberController {
    Logger logger = LoggerFactory.getLogger(getClass());
    private final MemberService service;

    // 회원가입 페이지
    @GetMapping({"/signup"})
    public String signUpPage() {
        logger.info("회원가입 페이지 접근");
        return "member/signup";
    }

    // 회원가입
    @PostMapping(value = "/api/members/signup")
    @ResponseBody
    public ResponseEntity<Void> signUp(@Valid @RequestBody SignupRequest request) {
        service.signUp(request);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    // 아이디 중복 체크
    @GetMapping(value = "/api/members/check-id")
    @ResponseBody
    public ResponseEntity<Map<String, Boolean>> checkId(@RequestParam String memberId) {
        boolean available = !service.existsByMemberId(memberId);
        return ResponseEntity.ok(Map.of("available", available));
    }

    // 이메일 중복체크
    @GetMapping("/api/members/check-email")
    @ResponseBody
    public ResponseEntity<Map<String, Boolean>> checkEmail(@RequestParam String email) {
        boolean available = !service.existsByEmail(email);
        return ResponseEntity.ok(Map.of("available", available));
    }

}
