package com.bcsd.community.controller.dto.response;

import com.bcsd.community.entity.Member;

public record MemberResponseDto(
        Long id,
        String username,
        String email
) {
    public static MemberResponseDto from(Member member) {
        return new MemberResponseDto(member.getId(), member.getUsername(), member.getEmail());
    }
}
