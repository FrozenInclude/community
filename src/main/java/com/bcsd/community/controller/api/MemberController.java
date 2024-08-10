package com.bcsd.community.controller.api;

import com.bcsd.community.controller.dto.request.MemberLoginRequestDto;
import com.bcsd.community.controller.dto.request.MemberRegisterRequestDto;
import com.bcsd.community.controller.dto.request.MemberUpdateRequestDto;
import com.bcsd.community.controller.dto.response.ArticleResponseDto;
import com.bcsd.community.controller.dto.response.BoardResponseDto;
import com.bcsd.community.controller.dto.response.MemberResponseDto;
import com.bcsd.community.entity.Member;
import com.bcsd.community.service.ArticleService;
import com.bcsd.community.service.CommentService;
import com.bcsd.community.service.MemberService;
import com.bcsd.community.util.PasswordUtils;
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
import jakarta.transaction.Transactional;
import lombok.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/member")
@Tag(name = "Member api", description = "회원과 관련된 엔드포인트를 제공합니다. 회원 정보는 세션을 통해 관리됩니다.")
public class MemberController {

    private final MemberService memberService;
    private final ArticleService articleService;
    private final CommentService commentService;
    private Member loginUser;

    @Operation(summary = "회원 가입", description = "회원을 등록합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "응답 성공",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = MemberResponseDto.class))),
            @ApiResponse(responseCode = "400 ", description = "예외 발생",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = GenericErrorResponse.class))),
            @ApiResponse(responseCode = "400", description = "필드 유효성 검사 실패",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ValidationErrorResponse.class)))
    })
    @PostMapping
    public ResponseEntity<?> register(
            @Validated @RequestBody MemberRegisterRequestDto request
    ) {
        return ResponseEntity.ok(memberService.register(request));
    }

    @Operation(summary = "로그인", description = "로그인 세션을 발급합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "응답 성공",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = MemberResponseDto.class))),
            @ApiResponse(responseCode = "400 ", description = "예외 발생",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = GenericErrorResponse.class))),
            @ApiResponse(responseCode = "400", description = "필드 유효성 검사 실패",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ValidationErrorResponse.class))),
            @ApiResponse(responseCode = "403", description = "로그인 세션 존재",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(type = "string", example = "이미 로그인되어 있습니다"))),
            @ApiResponse(responseCode = "401", description = "로그인 실패",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(type = "string", example = "유효하지 않은 이메일 또는 비밀번호 입니다.")))
    })
    @PostMapping("/login")
    public ResponseEntity<?> login(
            @RequestBody MemberLoginRequestDto request,
            HttpSession session
    ) {
        loginUser = (Member) session.getAttribute("loginUser");

        if (loginUser != null) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("이미 로그인되어 있습니다");
        }

        loginUser = memberService.login(request);

        if (loginUser == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("유효하지 않은 이메일 또는 비밀번호 입니다.");
        }

        session.setAttribute("loginUser", loginUser);
        return ResponseEntity.ok(MemberResponseDto.from(loginUser));
    }

    @Operation(summary = "정보 수정", description = "회원 정보를 수정합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "응답 성공",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = MemberResponseDto.class))),
            @ApiResponse(responseCode = "400 ", description = "예외 발생",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = GenericErrorResponse.class))),
            @ApiResponse(responseCode = "400", description = "필드 유효성 검사 실패",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ValidationErrorResponse.class))),
            @ApiResponse(responseCode = "401", description = "로그인 정보 없음",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(type = "string", example = "로그인이 필요합니다"))),
            @ApiResponse(responseCode = "401 ", description = "비밀번호 확인 불일치",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(type = "string", example = "비밀번호가 맞지 않습니다!")))
    })
    @PutMapping
    public ResponseEntity<?> updateMember(@Validated @RequestBody MemberUpdateRequestDto request,HttpSession httpSession) {
        if (!PasswordUtils.verifyPassword(request.confirm_password(), loginUser.getPassword(), loginUser.getSalt())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("비밀번호가 맞지 않습니다!");
        }
        memberService.edit(loginUser.getEmail(), request);
        loginUser.update(request);
        httpSession.setAttribute("loginUser", loginUser);
        return ResponseEntity.ok(memberService.edit(loginUser.getEmail(), request));
    }

    @Operation(summary = "회원 탈퇴", description = "회원을 탈퇴 합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "응답 성공",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(type = "string", example = "no content"))),
            @ApiResponse(responseCode = "400", description = "예외 발생",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = GenericErrorResponse.class))),
            @ApiResponse(responseCode = "401", description = "로그인 정보 없음",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(type = "string", example = "로그인이 필요합니다")))
    })
    @DeleteMapping
    public ResponseEntity<?> withdrawMember() {
        memberService.withDraw(loginUser.getEmail());
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "정보 조회", description = "회원 정보를 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "응답 성공",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = MemberResponseDto.class))),
            @ApiResponse(responseCode = "400", description = "예외 발생",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = GenericErrorResponse.class))),
            @ApiResponse(responseCode = "401", description = "로그인 정보 없음",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(type = "string", example = "로그인이 필요합니다")))
    })
    @GetMapping
    public ResponseEntity<?> userInfo(HttpSession session) {
        Member loginUser = (Member) session.getAttribute("loginUser");
        MemberResponseDto response = MemberResponseDto.from(loginUser);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "게시글 조회", description = "작성한 게시글을 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "응답 성공",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ArticleResponseDto.class))),
            @ApiResponse(responseCode = "400", description = "예외 발생",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = GenericErrorResponse.class))),
            @ApiResponse(responseCode = "401", description = "로그인 정보 없음",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(type = "string", example = "로그인이 필요합니다")))
    })
    @GetMapping("/articles")
    public ResponseEntity<?> getArticles(@Parameter(description = "검색어(제목+내용)") @RequestParam(required = false, value = "search") String search,
                                         @Parameter(description = "작성자") @RequestParam(required = false, value = "author") String author,
                                         @Parameter(description = "게시판 번호") @RequestParam(required = false, value = "authorId") Long boardId,
                                         @Parameter(description = "페이지 번호") @RequestParam(required = false, defaultValue = "0", value = "page") int pageNo,
                                         @Parameter(description = "정렬 기준") @RequestParam(required = false, defaultValue = "createdAt", value = "orderby") String criteria,
                                         @Parameter(description = "정렬 방향") @RequestParam(required = false, defaultValue = "DESC", value = "sort") String sort) {
        return ResponseEntity.ok(articleService.findAll(pageNo, criteria, sort, search, author, loginUser.getId(), boardId));
    }

    @Operation(summary = "게시판 조회", description = "생성한 게시판을 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "응답 성공",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = BoardResponseDto.class))),
            @ApiResponse(responseCode = "400", description = "예외 발생",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = GenericErrorResponse.class))),
            @ApiResponse(responseCode = "401", description = "로그인 정보 없음",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(type = "string", example = "로그인이 필요합니다")))
    })
    @GetMapping("/boards")
    public ResponseEntity<?> getBoards() {
        return ResponseEntity.ok(memberService.getBoards(loginUser.getEmail()));
    }

    @Operation(summary = "댓글 조회", description = "작성한 댓글을 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "응답 성공",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = MemberResponseDto.class))),
            @ApiResponse(responseCode = "400", description = "예외 발생",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = GenericErrorResponse.class))),
            @ApiResponse(responseCode = "401", description = "로그인 정보 없음",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(type = "string", example = "로그인이 필요합니다")))
    })
    @GetMapping("/comments")
    public ResponseEntity<?> getComments(@Parameter(description = "검색어(내용)") @RequestParam(required = false, value = "search") String search,
                                         @Parameter(description = "게시글 번호") @RequestParam(required = false, value = "articleId") Long articleId,
                                         @Parameter(description = "페이지 번호") @RequestParam(required = false, defaultValue = "0", value = "page") int pageNo,
                                         @Parameter(description = "정렬 방향(기준:)") @RequestParam(required = false, defaultValue = "DESC", value = "sort") String sort) {
        return ResponseEntity.ok(commentService.findAll(pageNo, sort, search, articleId, null, loginUser.getId()));
    }

    @Operation(summary = "로그아웃", description = "현재 로그인된 세션을 종료합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "로그아웃 성공",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(type = "string", example = "로그아웃 성공"))),
            @ApiResponse(responseCode = "401", description = "로그인 정보 없음",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(type = "string", example = "로그인이 필요합니다")))
    })
    @PostMapping("/logout")
    public ResponseEntity<String> logout(HttpSession session) {
        if (session.getAttribute("loginUser") == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("로그인이 필요합니다");
        }
        session.invalidate();
        loginUser = null;
        return ResponseEntity.ok("로그아웃 성공");
    }

}

