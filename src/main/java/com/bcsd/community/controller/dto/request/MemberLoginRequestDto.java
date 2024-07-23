package com.bcsd.community.controller.dto.request;

import jakarta.validation.constraints.*;

public record MemberLoginRequestDto(
        @NotBlank(message = "이메일을 입력해주세요.")
        @Email(message = "올바른 이메일 주소를 입력해주세요.")
        String email,

        @NotBlank(message = "비밀번호를 입력해주세요.")
        String password
) {
}