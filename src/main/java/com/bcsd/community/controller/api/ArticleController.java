package com.bcsd.community.controller.api;

import com.bcsd.community.controller.dto.request.ArticleUpdateRequestDto;
import com.bcsd.community.controller.dto.request.ArticleWriteRequestDto;
import com.bcsd.community.controller.dto.response.ArticleResponseDto;
import com.bcsd.community.controller.dto.response.BoardResponseDto;
import com.bcsd.community.controller.dto.response.MemberResponseDto;
import com.bcsd.community.entity.Member;
import com.bcsd.community.service.ArticleService;
import com.bcsd.community.service.CommentService;
import com.bcsd.community.util.swaggerModel.ArticleDtoList;
import com.bcsd.community.util.swaggerModel.CommentDtoList;
import com.bcsd.community.util.swaggerModel.GenericErrorResponse;
import com.bcsd.community.util.swaggerModel.ValidationErrorResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpSession;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@AllArgsConstructor
@RestController
@RequestMapping("/api/article")
@Tag(name = "Article api", description = "게시글과 관련된 엔드포인트를 제공합니다.")
public class ArticleController {

    private final ArticleService articleService;
    private final CommentService commentService;

    @Operation(summary = "게시물 조회", description = "게시글을 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "응답 성공",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ArticleResponseDto.class))),
            @ApiResponse(responseCode = "400 ", description = "예외 발생",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = GenericErrorResponse.class)))
    })
    @GetMapping("/{id}")
    public ResponseEntity<?> getByIdOnArticle(@PathVariable Long id) {
        return ResponseEntity.ok(articleService.findById(id));
    }

    @Operation(summary = "게시글 조회", description = "게시글을 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "응답 성공",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ArticleDtoList.class))),
            @ApiResponse(responseCode = "400", description = "예외 발생",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = GenericErrorResponse.class)))
    })
    @GetMapping
    public ResponseEntity<?> getAllOnArticle(@Parameter(description = "검색어(제목+내용)") @RequestParam(required = false, value = "search") String search,
                                             @Parameter(description = "작성자") @RequestParam(required = false, value = "author") String author,
                                             @Parameter(description = "작성자 번호") @RequestParam(required = false, value = "authorId") Long authorId,
                                             @Parameter(description = "게시판 번호") @RequestParam(required = false, value = "boardId") Long boardId,
                                             @Parameter(description = "페이지 번호") @RequestParam(required = false, defaultValue = "0", value = "page") int pageNo,
                                             @Parameter(description = "정렬 기준") @RequestParam(required = false, defaultValue = "createdAt", value = "orderby") String criteria,
                                             @Parameter(description = "정렬 방향") @RequestParam(required = false, defaultValue = "DESC", value = "sort") String sort) {
        return ResponseEntity.ok(articleService.findAll(pageNo, criteria, sort, search, author, authorId, boardId));
    }

    @Operation(summary = "댓글 조회", description = "게시글의 댓글들을 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "응답 성공",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CommentDtoList.class))),
            @ApiResponse(responseCode = "400", description = "예외 발생",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = GenericErrorResponse.class)))
    })
    @GetMapping("/{id}/comments")
    public ResponseEntity<?> getCommentsOnArticle(@PathVariable Long id,
                                                  @Parameter(description = "검색어(내용)") @RequestParam(required = false, value = "search") String search,
                                                  @Parameter(description = "작성자") @RequestParam(required = false, value = "author") String author,
                                                  @Parameter(description = "작성자 번호") @RequestParam(required = false, value = "authorId") Long authorId,
                                                  @Parameter(description = "페이지 번호") @RequestParam(required = false, defaultValue = "0", value = "page") int pageNo,
                                                  @Parameter(description = "정렬 방향(기준:)") @RequestParam(required = false, defaultValue = "DESC", value = "sort") String sort) {
        return ResponseEntity.ok(commentService.findAll(pageNo, sort, search, id, author, authorId));
    }

    @Operation(summary = "작성자 조회", description = "게시글의 작성자를 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "응답 성공",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = MemberResponseDto.class))),
            @ApiResponse(responseCode = "400", description = "예외 발생",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = GenericErrorResponse.class)))
    })
    @GetMapping("/{id}/author")
    public ResponseEntity<?> getAuthorOnArticle(@PathVariable Long id) {
        return ResponseEntity.ok(articleService.getAuthor(id));
    }

    @Operation(summary = "게시판 조회", description = "속한 게시판을 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "응답 성공",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = BoardResponseDto.class))),
            @ApiResponse(responseCode = "400", description = "예외 발생",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = GenericErrorResponse.class)))
    })
    @GetMapping("/{id}/board")
    public ResponseEntity<?> getBoardOnArticle(@PathVariable Long id) {
        return ResponseEntity.ok(articleService.getBoard(id));
    }

    @Operation(summary = "게시글 작성", description = "게시판에 게시글을 작성합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "응답 성공",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ArticleResponseDto.class))),
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
    public ResponseEntity<?> writeOnArticle(
            @Validated @RequestBody ArticleWriteRequestDto request,
            HttpSession session
    ) {
        Member loginUser = (Member) session.getAttribute("loginUser");
        return ResponseEntity.ok(articleService.write(loginUser, request));
    }

    @Operation(summary = "게시글 수정", description = "게시글을 수정합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "응답 성공",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ArticleResponseDto.class))),
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
    public ResponseEntity<?> editOnArticle(@PathVariable Long id,
                                           @Validated @RequestBody ArticleUpdateRequestDto request) {
        return ResponseEntity.ok(articleService.edit(id, request));
    }

    @Operation(summary = "게시글 삭제", description = "게시글을 삭제합니다.")
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
    public ResponseEntity<?> deleteOnArticle(@PathVariable Long id) {
        articleService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
