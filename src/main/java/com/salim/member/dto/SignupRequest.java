package com.salim.member.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record SignupRequest(@Pattern(regexp = "^[a-z][a-z0-9]{3,19}$", message = "아이디는 영문 소문자로 시작하는 4~20자입니다.")
                            String memberId,

                            @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*[!@#$%^&*()_+\\-=\\[\\]{}|;:,.<>?]).{8,}$",
                                    message = "비밀번호는 영문과 특수문자를 포함해 8자 이상이어야 합니다.")
                            String password,

                            @Email @NotBlank String email,

                            @NotBlank String name) {
}
