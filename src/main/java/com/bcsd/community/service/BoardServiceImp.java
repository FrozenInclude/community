package com.bcsd.community.service;

import com.bcsd.community.controller.dto.request.BoardCreateRequestDto;
import com.bcsd.community.controller.dto.request.BoardUpdateRequestDto;
import com.bcsd.community.controller.dto.response.ArticleResponseDto;
import com.bcsd.community.controller.dto.response.BoardResponseDto;
import com.bcsd.community.controller.dto.response.MemberResponseDto;
import com.bcsd.community.entity.Board;
import com.bcsd.community.entity.Member;
import com.bcsd.community.repository.BoardRepository;
import com.bcsd.community.service.exception.AlreadyExistsException;
import com.bcsd.community.service.exception.BoardNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;


@Service
@AllArgsConstructor
public class BoardServiceImp implements BoardService {

    private final BoardRepository boardRepository;

    @Override
    @Transactional(readOnly = true)
    public BoardResponseDto findById(Long board_id) {
        Board board = boardRepository.findById(board_id)
                .orElseThrow(BoardNotFoundException::new);
        return BoardResponseDto.from(board);
    }

    @Override
    @Transactional(readOnly = true)
    public List<BoardResponseDto> findAll() {
        List<Board> boards = boardRepository.findAll();
        return BoardResponseDto.from_list(boards);
    }

    @Override
    @Transactional
    public BoardResponseDto create(Member author, BoardCreateRequestDto request) {
        boardRepository.findByBoardName(request.boardName())
                .ifPresent(email -> {
                    throw new AlreadyExistsException("boardName");
                });
        Board board = boardRepository.save(request.toEntity());
        board.setAuthor(author);
        return BoardResponseDto.from(board);
    }

    @Override
    @Transactional
    public BoardResponseDto edit(Long id, BoardUpdateRequestDto request) {
        Board board = boardRepository.findById(id).orElseThrow(BoardNotFoundException::new);
        board.update(request);
        return BoardResponseDto.from(board);
    }

    @Override
    @Transactional
    public void delete(Long board_id) {
        Board board = boardRepository.findById(board_id)
                .orElseThrow(BoardNotFoundException::new);
        boardRepository.delete(board);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ArticleResponseDto> getArticles(Long board_id) {
        return ArticleResponseDto.from_list(boardRepository.findById(board_id)
                .orElseThrow(BoardNotFoundException::new).getArticles());
    }

    @Override
    @Transactional(readOnly = true)
    public MemberResponseDto getAuthor(Long board_id) {
        return MemberResponseDto.from(boardRepository.findById(board_id)
                .orElseThrow(BoardNotFoundException::new).getAuthor());
    }

}
