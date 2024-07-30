package com.bcsd.community.controller.dto.request;

import jakarta.validation.constraints.Size;

public record ArticleUpdateRequestDto(
        @Size(min = 1, max = 100, message = "제목은 1자에서 100자 사이여야 합니다.")
        String title,

        @Size(min = 1, max = 1000, message = "내용은 1자에서 1000자 사이여야 합니다.")
        String content
) {
}
