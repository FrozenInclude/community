package com.bcsd.community.controller.dto.response;

import com.bcsd.community.entity.Article;

import java.time.LocalDateTime;

public record ArticleResponseDto(
        Long id,
        String title,
        String content,
        String author,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
    public static ArticleResponseDto from(Article article) {
        return new ArticleResponseDto(article.getId(), article.getTitle(),
                article.getContent(),article.getMember().getUsername(),article.getCreatedAt(), article.getUpdatedAt());
    }
}

