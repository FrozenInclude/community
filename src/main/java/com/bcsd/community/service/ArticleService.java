package com.bcsd.community.service;

import com.bcsd.community.controller.dto.request.ArticleUpdateRequestDto;
import com.bcsd.community.controller.dto.request.ArticleWriteRequestDto;
import com.bcsd.community.controller.dto.response.ArticleResponseDto;
import com.bcsd.community.controller.dto.response.BoardResponseDto;
import com.bcsd.community.controller.dto.response.CommentResponseDto;
import com.bcsd.community.controller.dto.response.MemberResponseDto;
import com.bcsd.community.entity.Member;

import java.util.List;

public interface ArticleService {
    ArticleResponseDto findById(Long article_id);

    List<ArticleResponseDto> findAll();

    ArticleResponseDto write(Member author, ArticleWriteRequestDto request);

    ArticleResponseDto edit(Long article_id, ArticleUpdateRequestDto request);

    void delete(Long article_id);

    List<CommentResponseDto> getComments(Long article_id);

    MemberResponseDto getAuthor(Long article_id);

    BoardResponseDto getBoard(Long article_id);
}
