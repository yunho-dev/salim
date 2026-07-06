package com.sallim.member.dto;

import com.sallim.member.entity.Member;

public record MemberResponse(
        String memberId,
        String nickname,
        String email
) {
    public static MemberResponse from(Member member) {
        return new MemberResponse(member.getMemberId(), member.getNickname(), member.getEmail());
    }
}