package com.bcsd.community.controller.dto.request;

import com.bcsd.community.entity.Member;
import com.bcsd.community.util.PasswordUtils;
import jakarta.validation.constraints.*;

import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;

public record MemberRegisterRequestDto(
        @NotBlank(message = "이름을 입력해주세요.")
        @Size(min = 2, max = 30, message = "이름은 2자 이상, 30자 이하로 입력해주세요.")
        String username,

        @NotBlank(message = "이메일을 입력해주세요.")
        @Email(message = "올바른 이메일 주소를 입력해주세요.")
        String email,

        @NotBlank(message = "비밀번호를 입력해주세요.")
        @Size(min = 8, message = "비밀번호는 최소 8자 이상이어야 합니다.")
        @Pattern(regexp = ".*[A-Z].*", message = "비밀번호에는 최소 하나의 대문자가 포함되어야 합니다.")
        @Pattern(regexp = ".*[a-z].*", message = "비밀번호에는 최소 하나의 소문자가 포함되어야 합니다.")
        @Pattern(regexp = ".*\\d.*", message = "비밀번호에는 최소 하나의 숫자가 포함되어야 합니다.")
        @Pattern(regexp = ".*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>\\/?].*", message = "비밀번호에는 최소 하나의 특수 문자가 포함되어야 합니다.")
        String password
) {
    public Member toEntity(){
        String salt = PasswordUtils.generateSalt();
        return Member.builder()
                .username(username)
                .password(PasswordUtils.hashPasswordWithSalt(password, salt))
                .email(email)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .salt(salt)
                .build();
    }
}
