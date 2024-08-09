package com.bcsd.community.service;

import com.bcsd.community.controller.dto.request.CommentWriteRequestDto;
import com.bcsd.community.controller.dto.response.ArticleResponseDto;
import com.bcsd.community.controller.dto.response.CommentResponseDto;
import com.bcsd.community.controller.dto.response.MemberResponseDto;
import com.bcsd.community.entity.Article;
import com.bcsd.community.entity.Comment;
import com.bcsd.community.entity.Member;
import com.bcsd.community.repository.ArticleRepository;
import com.bcsd.community.repository.CommentRepository;
import com.bcsd.community.service.exception.ArticleNotFoundException;
import com.bcsd.community.service.exception.CommentNotFoundException;
import com.bcsd.community.util.ArticleSpecification;
import com.bcsd.community.util.CommentSpecification;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@AllArgsConstructor
public class CommentServiceImp implements CommentService{

    private final CommentRepository commentRepository;
    private final ArticleRepository articleRepository;

    @Override
    @Transactional(readOnly = true)
    public CommentResponseDto findById(Long comment_id){
        Comment comment=commentRepository.findById(comment_id)
                .orElseThrow(CommentNotFoundException::new);
        return CommentResponseDto.from(comment);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CommentResponseDto> findAll(int pageNo, String sort, String search, Long articleId,String author, Long authorId){
        String criteria="createdAt";

        Pageable pageable = (sort.equalsIgnoreCase("asc")) ?
                PageRequest.of(pageNo, 10, Sort.by(Sort.Direction.ASC, criteria))
                : PageRequest.of(pageNo, 10, Sort.by(Sort.Direction.DESC, criteria));

        Specification<Comment> spec = CommentSpecification.hasKeywordInTitleOrContent(search)
                .and(CommentSpecification.hasAuthor(author))
                .and(CommentSpecification.hasArticleId(articleId))
                .and(CommentSpecification.hasMemberId(authorId));

        return commentRepository.findAll(spec, pageable).map(CommentResponseDto::from).getContent();
    }

    @Override
    @Transactional
    public CommentResponseDto write(Member author, CommentWriteRequestDto request){
        Comment comment=request.toEntity();
        comment.setCommentAuthor(author);
        comment.setClassArticle(articleRepository.findById(request.articleId())
                .orElseThrow(ArticleNotFoundException::new));
        return  CommentResponseDto.from(commentRepository.save(comment));
    }

    @Override
    @Transactional
    public void delete(Long comment_id){
        Comment comment=commentRepository.findById(comment_id)
                .orElseThrow(CommentNotFoundException::new);
        commentRepository.delete(comment);
    }
    @Override
    @Transactional(readOnly = true)
    public MemberResponseDto getAuthor(Long comment_id){
        return MemberResponseDto.from(commentRepository.findById(comment_id)
                .orElseThrow(CommentNotFoundException::new).getCommentAuthor());
    }

    @Override
    @Transactional(readOnly = true)
    public ArticleResponseDto getArticle(Long comment_id){
        return ArticleResponseDto.from(commentRepository.findById(comment_id)
                .orElseThrow(CommentNotFoundException::new).getClassArticle());
    }
}
