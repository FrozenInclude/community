package com.bcsd.community.controller.dto.request;

import com.bcsd.community.entity.Board;
import com.bcsd.community.entity.Member;
import com.bcsd.community.util.PasswordUtils;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;

public record BoardCreateRequestDto(
        @NotBlank(message = "게시판 이름을 입력해주세요.")
        @Size(min = 2, max = 20, message = "게시판 이름은 2자 이상, 20자 이하로 입력해주세요.")
        String boardName,

        @NotBlank(message = "게시판 설명을 입력해주세요.")
        @Size(min = 2, max = 50, message = "게시판 설명은 2자 이상, 50자 이하로 입력해주세요.")
        String description
) {
    public Board toEntity(){
        return Board.builder()
                .boardName(boardName)
                .description(description)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
    }
}
