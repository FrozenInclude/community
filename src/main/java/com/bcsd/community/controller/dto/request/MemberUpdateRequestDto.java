package com.bcsd.community.controller.dto.request;

public record MemberUpdateRequestDto(
        String username,
        String email,
        String password
) {
}
