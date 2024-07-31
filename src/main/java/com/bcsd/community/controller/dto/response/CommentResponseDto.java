package com.bcsd.community.controller.dto.response;

import com.bcsd.community.entity.Comment;

import java.time.LocalDateTime;

public record CommentResponseDto(
        Long id,
        String content,
        LocalDateTime createdAt
) {
    public static CommentResponseDto from(Comment comment) {
        return new CommentResponseDto(comment.getId(),comment.getContent(),comment.getCreatedAt());
    }
}
