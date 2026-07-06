package com.sallim.member.controller;

import com.sallim.member.dto.SignupRequest;
import com.sallim.member.service.MemberService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api/members")
@RequiredArgsConstructor // 생성자 주입
public class MemberApiController {
    private final MemberService memberService;

    // 회원가입
    @PostMapping(value = "/signup")
    public ResponseEntity<Void> signUp(@Valid @RequestBody SignupRequest request) {
        log.info("회원가입 요청: memberId={}", request.memberId());
        memberService.signUp(request);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    // 아이디 중복 체크
    @GetMapping(value = "/check-id")
    public ResponseEntity<Map<String, Boolean>> checkId(@RequestParam String memberId) {
        boolean available = !memberService.existsByMemberId(memberId);
        log.debug("아이디 중복 체크: memberId={}, available={}", memberId, available);
        return ResponseEntity.ok(Map.of("available", available));
    }

    // 이메일 중복체크
    @GetMapping("/check-email")
    public ResponseEntity<Map<String, Boolean>> checkEmail(@RequestParam String email) {
        boolean available = !memberService.existsByEmail(email);
        log.debug("이메일 중복 체크: email={}, available={}", email, available);
        return ResponseEntity.ok(Map.of("available", available));
    }

}
