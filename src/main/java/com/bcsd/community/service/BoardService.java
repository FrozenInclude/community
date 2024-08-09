package com.bcsd.community.service;

import com.bcsd.community.controller.dto.request.BoardCreateRequestDto;
import com.bcsd.community.controller.dto.request.BoardUpdateRequestDto;
import com.bcsd.community.controller.dto.response.ArticleResponseDto;
import com.bcsd.community.controller.dto.response.BoardResponseDto;
import com.bcsd.community.controller.dto.response.MemberResponseDto;
import com.bcsd.community.entity.Member;

import java.util.List;

public interface BoardService {
    BoardResponseDto findById(Long board_id);

    List<BoardResponseDto> findAll();

    BoardResponseDto create(Member author, BoardCreateRequestDto request);

    BoardResponseDto edit(Long board_id, BoardUpdateRequestDto request);

    void delete(Long board_id);

    MemberResponseDto getAuthor(Long board_id);
}
