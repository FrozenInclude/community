package com.bcsd.community.controller.dto.response;

import com.bcsd.community.entity.Article;

import java.time.LocalDateTime;

public record ArticleResponseDto(
        Long id,
        String title,
        String content,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
    public static ArticleResponseDto from(Article article) {
        return new ArticleResponseDto(article.getId(), article.getTitle(),
                article.getContent(), article.getCreatedAt(), article.getUpdatedAt());
    }
}

