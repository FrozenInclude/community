package com.bcsd.community.controller.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record MemberUpdateRequestDto(
        @NotBlank(message = "기존 비밀번호를 입력하세요.")
        String confirm_password,
        @Size(min = 2, max = 30, message = "이름은 2자 이상, 30자 이하로 입력해주세요.")
        String username,

        @Size(min = 8, message = "비밀번호는 최소 8자 이상이어야 합니다.")
        @Pattern(regexp = ".*[A-Z].*", message = "비밀번호에는 최소 하나의 대문자가 포함되어야 합니다.")
        @Pattern(regexp = ".*[a-z].*", message = "비밀번호에는 최소 하나의 소문자가 포함되어야 합니다.")
        @Pattern(regexp = ".*\\d.*", message = "비밀번호에는 최소 하나의 숫자가 포함되어야 합니다.")
        @Pattern(regexp = ".*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>\\/?].*", message = "비밀번호에는 최소 하나의 특수 문자가 포함되어야 합니다.")
        String password
) {
}
