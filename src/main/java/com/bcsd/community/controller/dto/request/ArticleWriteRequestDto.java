package com.bcsd.community.controller.dto.request;

import com.bcsd.community.entity.Article;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;

public record ArticleWriteRequestDto(
        @NotBlank(message = "제목을 입력해주세요")
        @Size(min = 2, max = 20, message = "제목은 2자 이상, 20자 이하로 입력해주세요")
        String title,

        @NotBlank(message = "내용을 입력해주세요")
        String content,

        @NotBlank(message = "게시판을 선택해 주세요")
        Long boardId
) {
    public Article toEntity(){
        return Article.builder()
                .title(title)
                .content(content)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
    }
}
