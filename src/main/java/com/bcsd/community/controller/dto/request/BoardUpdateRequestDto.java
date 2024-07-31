package com.bcsd.community.controller.dto.request;

import jakarta.validation.constraints.Size;

public record BoardUpdateRequestDto(
        @Size(min = 2, max = 20, message = "게시판 이름은 2자 이상, 20자 이하로 입력해주세요.")
        String boardName,

        @Size(min = 2, max = 50, message = "게시판 설명은 2자 이상, 50자 이하로 입력해주세요.")
        String description
) {
}
