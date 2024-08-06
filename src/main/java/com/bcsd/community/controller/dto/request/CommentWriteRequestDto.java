package com.bcsd.community.controller.dto.request;

import com.bcsd.community.entity.Article;
import com.bcsd.community.entity.Comment;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;

public record CommentWriteRequestDto(
        @NotBlank(message = "내용을 입력해주세요")
        @Size(min = 1, max = 100, message = "댓글은 100자 이하로 입력해주세요.")
        String content,

        @NotBlank(message = "게시글을 선택해 주세요")
        Long articleId
) {
        public Comment toEntity(){
                return Comment.builder()
                        .content(content)
                        .createdAt(LocalDateTime.now())
                        .build();
        }
}
