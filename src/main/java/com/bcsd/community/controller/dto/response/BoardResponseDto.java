package com.bcsd.community.controller.dto.response;

import com.bcsd.community.entity.Article;
import com.bcsd.community.entity.Board;

import java.time.LocalDateTime;
import java.util.List;

public record BoardResponseDto(
        Long id,
        String boardName,
        String description,
        String author,
        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        List<ArticleResponseDto> articles
) {
    public static BoardResponseDto from(Board board) {
        return new BoardResponseDto(board.getId(), board.getBoardName(), board.getDescription(),
                board.getAuthor().getUsername(),board.getCreatedAt(), board.getUpdatedAt(),
                ArticleResponseDto.from_list(board.getArticles()));
    }
}
