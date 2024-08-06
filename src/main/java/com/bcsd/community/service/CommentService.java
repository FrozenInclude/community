package com.bcsd.community.service;

import com.bcsd.community.controller.dto.request.CommentWriteRequestDto;
import com.bcsd.community.controller.dto.response.ArticleResponseDto;
import com.bcsd.community.controller.dto.response.CommentResponseDto;
import com.bcsd.community.controller.dto.response.MemberResponseDto;
import com.bcsd.community.entity.Member;

import java.util.List;

public interface CommentService {
    CommentResponseDto findById(Long comment_id);

    List<CommentResponseDto> findAll();

    CommentResponseDto write(Member author, CommentWriteRequestDto request);

    void delete(Long comment_id);

    MemberResponseDto getAuthor(Long comment_id);

    ArticleResponseDto getArticle(Long comment_id);
}
