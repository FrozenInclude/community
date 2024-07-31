package com.bcsd.community.controller.dto.response;

import com.bcsd.community.entity.Board;

import java.time.LocalDateTime;

public record BoardResponseDto(
        Long id,
        String board_name,
        String description,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
    public static BoardResponseDto from(Board board) {
        return new BoardResponseDto(board.getId(), board.getBoard_name(), board.getDescription(),
                board.getCreatedAt(), board.getUpdatedAt());
    }
}
