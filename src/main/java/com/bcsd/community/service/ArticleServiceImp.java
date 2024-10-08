package com.bcsd.community.service;

import com.bcsd.community.controller.dto.request.ArticleUpdateRequestDto;
import com.bcsd.community.controller.dto.request.ArticleWriteRequestDto;
import com.bcsd.community.controller.dto.response.ArticleResponseDto;
import com.bcsd.community.controller.dto.response.BoardResponseDto;
import com.bcsd.community.controller.dto.response.CommentResponseDto;
import com.bcsd.community.controller.dto.response.MemberResponseDto;
import com.bcsd.community.entity.Article;
import com.bcsd.community.entity.Board;
import com.bcsd.community.entity.Member;
import com.bcsd.community.repository.ArticleRepository;
import com.bcsd.community.repository.BoardRepository;
import com.bcsd.community.service.exception.ArticleNotFoundException;
import com.bcsd.community.service.exception.BoardNotFoundException;
import com.bcsd.community.util.ArticleSpecification;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.function.LongFunction;

@Service
@AllArgsConstructor
public class ArticleServiceImp implements ArticleService {

    private final ArticleRepository articleRepository;
    private final BoardRepository boardRepository;

    @Override
    @Transactional(readOnly = true)
    public ArticleResponseDto findById(Long article_id) {
        Article article = articleRepository.findById(article_id)
                .orElseThrow(ArticleNotFoundException::new);
        return ArticleResponseDto.from(article);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ArticleResponseDto> findAll(int pageNo, String criteria, String sort, String search, String author, Long authorId,
                                            Long boardId) {
        Pageable pageable = (sort.equalsIgnoreCase("asc")) ?
                PageRequest.of(pageNo, 10, Sort.by(Sort.Direction.ASC, criteria))
                : PageRequest.of(pageNo, 10, Sort.by(Sort.Direction.DESC, criteria));

        Specification<Article> spec = ArticleSpecification.hasKeywordInTitleOrContent(search)
                .and(ArticleSpecification.hasAuthor(author))
                .and(ArticleSpecification.hasBoardId(boardId))
                .and(ArticleSpecification.hasMemberId(authorId));

        return articleRepository.findAll(spec, pageable).map(ArticleResponseDto::from).getContent();
    }

    @Override
    @Transactional
    public ArticleResponseDto write(Member author, ArticleWriteRequestDto request) {
        Article article = request.toEntity();
        article.setBoard(boardRepository.findById(request.boardId())
                .orElseThrow(BoardNotFoundException::new));
        article.setMember(author);
        return ArticleResponseDto.from(articleRepository.save(article));
    }

    @Override
    @Transactional
    public ArticleResponseDto edit(Long article_id, ArticleUpdateRequestDto request) {
        Article article = articleRepository.findById(article_id).orElseThrow(ArticleNotFoundException::new);
        article.update(request);
        return ArticleResponseDto.from(article);
    }

    @Override
    @Transactional
    public void delete(Long article_id) {
        Article article = articleRepository.findById(article_id)
                .orElseThrow(ArticleNotFoundException::new);
        articleRepository.delete(article);
    }


    @Override
    public MemberResponseDto getAuthor(Long article_id) {
        return MemberResponseDto.from(articleRepository.findById(article_id)
                .orElseThrow(ArticleNotFoundException::new).getMember());
    }

    @Override
    public BoardResponseDto getBoard(Long article_id) {
        return BoardResponseDto.from(articleRepository.findById(article_id)
                .orElseThrow(ArticleNotFoundException::new).getBoard());
    }
}
