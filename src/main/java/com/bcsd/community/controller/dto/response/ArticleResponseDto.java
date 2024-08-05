package com.bcsd.community.controller.dto.response;

import com.bcsd.community.entity.Article;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

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
                article.getContent(), article.getMember().getUsername(), article.getCreatedAt(), article.getUpdatedAt());
    }

    public static List<ArticleResponseDto> from_list(List<Article> articles) {
        return articles.stream()
                .map(ArticleResponseDto::from)
                .collect(Collectors.toList());
    }
}

