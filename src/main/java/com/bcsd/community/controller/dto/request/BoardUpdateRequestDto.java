package com.bcsd.community.controller.dto.request;

public record BoardUpdateRequestDto(
        String board_name,

        String description
) {
}
