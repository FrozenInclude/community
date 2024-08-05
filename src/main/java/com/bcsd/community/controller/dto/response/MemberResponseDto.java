package com.bcsd.community.controller.dto.response;

import com.bcsd.community.entity.Comment;
import com.bcsd.community.entity.Member;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public record MemberResponseDto(
        Long id,
        String username,
        String email,
        LocalDateTime createdAt
) {
    public static MemberResponseDto from(Member member) {
        return new MemberResponseDto(member.getId(), member.getUsername(), member.getEmail(), member.getCreatedAt());
    }

    public static List<MemberResponseDto> from_list(List<Member> members) {
        return members.stream()
                .map(MemberResponseDto::from)
                .collect(Collectors.toList());
    }
}
