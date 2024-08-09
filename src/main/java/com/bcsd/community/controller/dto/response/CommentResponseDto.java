package com.bcsd.community.controller.dto.response;

import com.bcsd.community.entity.Article;
import com.bcsd.community.entity.Comment;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public record CommentResponseDto(
        Long id,
        Long articleId,
        String content,
        String author,
        LocalDateTime createdAt
) {
    public static CommentResponseDto from(Comment comment) {
        return new CommentResponseDto(comment.getId(), comment.getClassArticle().getId(),comment.getContent(),
                comment.getCommentAuthor().getUsername(), comment.getCreatedAt());
    }

    public static List<CommentResponseDto> from_list(List<Comment> comments) {
        return comments.stream()
                .map(CommentResponseDto::from)
                .collect(Collectors.toList());
    }
}
