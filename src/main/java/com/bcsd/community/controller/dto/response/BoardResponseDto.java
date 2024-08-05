package com.bcsd.community.controller.dto.response;

import com.bcsd.community.entity.Article;
import com.bcsd.community.entity.Board;
import com.bcsd.community.entity.Comment;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

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
                board.getAuthor().getUsername(), board.getCreatedAt(), board.getUpdatedAt(),
                ArticleResponseDto.from_list(board.getArticles()));
    }

    public static List<BoardResponseDto> from_list(List<Board> boards) {
        return boards.stream()
                .map(BoardResponseDto::from)
                .collect(Collectors.toList());
    }
}
