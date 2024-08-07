package com.bcsd.community.controller.api;

import com.bcsd.community.controller.dto.request.BoardCreateRequestDto;
import com.bcsd.community.controller.dto.request.BoardUpdateRequestDto;
import com.bcsd.community.controller.dto.response.ArticleResponseDto;
import com.bcsd.community.controller.dto.response.BoardResponseDto;
import com.bcsd.community.controller.dto.response.MemberResponseDto;
import com.bcsd.community.entity.Member;
import com.bcsd.community.service.BoardService;
import com.bcsd.community.util.swaggerModel.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpSession;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;

@AllArgsConstructor
@RestController
@RequestMapping("/api/board")
@Tag(name = "Board api", description = "게시판과 관련된 엔드포인트를 제공합니다.")
public class BoardController {

    private final BoardService boardService;

    @Operation(summary = "게시판 조회", description = "게시판을 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "응답 성공",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = BoardResponseDto.class))),
            @ApiResponse(responseCode = "400 ", description = "예외 발생",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = GenericErrorResponse.class)))
    })
    @GetMapping("/{id}")
    public ResponseEntity<?> getByIdOnBoard(@PathVariable Long id) {
        return ResponseEntity.ok(boardService.findById(id));
    }

    @Operation(summary = "게시판 전체 조회", description = "모든 게시판을 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "응답 성공",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = BoardDtoList.class))),
            @ApiResponse(responseCode = "400 ", description = "예외 발생",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = GenericErrorResponse.class)))
    })
    @GetMapping
    public ResponseEntity<?> getAllOnBoard() {
        return ResponseEntity.ok(boardService.findAll());
    }

    @Operation(summary = "게시글 조회", description = "게시판의 게시글들을 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "응답 성공",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ArticleDtoList.class))),
            @ApiResponse(responseCode = "400", description = "예외 발생",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = GenericErrorResponse.class)))
    })
    @GetMapping("/{id}/articles")
    public ResponseEntity<?> getArticlesOnBoard(@PathVariable Long id) {
        return ResponseEntity.ok(boardService.getArticles(id));
    }

    @Operation(summary = "생성자 조회", description = "게시판의 생성자를 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "응답 성공",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = MemberResponseDto.class))),
            @ApiResponse(responseCode = "400", description = "예외 발생",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = GenericErrorResponse.class)))
    })
    @GetMapping("/{id}/author")
    public ResponseEntity<?> getAuthorOnBoard(@PathVariable Long id) {
        return ResponseEntity.ok(boardService.getAuthor(id));
    }

    @Operation(summary = "게시판 생성", description = "게시판을 생성합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "응답 성공",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = BoardResponseDto.class))),
            @ApiResponse(responseCode = "400", description = "필드 유효성 검사 실패",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ValidationErrorResponse.class))),
            @ApiResponse(responseCode = "400 ", description = "예외 발생",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = GenericErrorResponse.class))),
            @ApiResponse(responseCode = "401", description = "로그인 정보 없음",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(type = "string", example = "로그인이 필요합니다")))
    })
    @PostMapping
    public ResponseEntity<?> createOnBoard(
            @Validated @RequestBody BoardCreateRequestDto request,
            HttpSession session
    ) {
        Member loginUser = (Member) session.getAttribute("loginUser");
        return ResponseEntity.ok(boardService.create(loginUser, request));
    }

    @Operation(summary = "게시판 수정", description = "게시판 정보를 수정합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "응답 성공",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = BoardResponseDto.class))),
            @ApiResponse(responseCode = "400", description = "필드 유효성 검사 실패",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ValidationErrorResponse.class))),
            @ApiResponse(responseCode = "400 ", description = "예외 발생",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = GenericErrorResponse.class))),
            @ApiResponse(responseCode = "403", description = "권한 없음",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(type = "string", example = "잘못된 접근입니다")))
    })
    @PutMapping("/{id}")
    public ResponseEntity<?> editOnBoard(@PathVariable Long id,
                                  @Validated @RequestBody BoardUpdateRequestDto request,
                                  HttpSession session
    ) {
        Member loginUser = (Member) session.getAttribute("loginUser");
        return ResponseEntity.ok(boardService.edit(id, request));
    }

    @Operation(summary = "게시판 삭제", description = "게시판을 삭제합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "응답 성공",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(type = "string", example = "no content"))),
            @ApiResponse(responseCode = "400", description = "예외 발생",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = GenericErrorResponse.class))),
            @ApiResponse(responseCode = "403", description = "권한 없음",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(type = "string", example = "잘못된 접근입니다")))
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteOnBoard(@PathVariable Long id,
                                    HttpSession session
    ) {
        Member loginUser = (Member) session.getAttribute("loginUser");
        boardService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
