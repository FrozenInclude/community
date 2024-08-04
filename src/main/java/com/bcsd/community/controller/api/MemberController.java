package com.bcsd.community.controller.api;

import com.bcsd.community.controller.dto.request.MemberLoginRequestDto;
import com.bcsd.community.controller.dto.request.MemberRegisterRequestDto;
import com.bcsd.community.controller.dto.request.MemberUpdateRequestDto;
import com.bcsd.community.controller.dto.response.MemberResponseDto;
import com.bcsd.community.entity.Member;
import com.bcsd.community.service.MemberService;
import jakarta.servlet.http.HttpSession;
import lombok.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@AllArgsConstructor
@RestController
@RequestMapping("/api/member")
public class MemberController {

    private final MemberService memberService;

    @PostMapping
    public ResponseEntity<?> register(
            @Validated @RequestBody MemberRegisterRequestDto request
    ) {
        return ResponseEntity.ok(memberService.register(request));
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(
            @RequestBody MemberLoginRequestDto request,
            HttpSession session
    ) {
        Member loginUser = (Member) session.getAttribute("loginUser");
        if (loginUser != null) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("이미 로그인되어 있습니다");
        }

        loginUser = memberService.login(request);
        if (loginUser == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("유효하지 않은 이메일 또는 비밀번호 입니다.");
        }

        session.setAttribute("loginUser", loginUser);
        return ResponseEntity.ok(loginUser);
    }

    @PutMapping
    public ResponseEntity<?> updateMember(
            @Validated @RequestBody MemberUpdateRequestDto request,
            HttpSession session
    ) {
        Member loginUser = (Member) session.getAttribute("loginUser");
        loginUser.update(request);
        MemberResponseDto response = MemberResponseDto.from(loginUser);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping
    public ResponseEntity<?> withdrawMember(
            HttpSession session
    ) {
        Member loginUser = (Member) session.getAttribute("loginUser");
        memberService.withDraw(loginUser.getEmail());
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<?> userInfo(HttpSession session) {
        Member loginUser = (Member) session.getAttribute("loginUser");
        MemberResponseDto response = MemberResponseDto.from(loginUser);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/articles")
    public ResponseEntity<?> getArticles(HttpSession session) {
        Member loginUser = (Member) session.getAttribute("loginUser");
        return ResponseEntity.ok(memberService.getArticles(loginUser.getEmail()));
    }

    @GetMapping("/boards")
    public ResponseEntity<?> getBoards(HttpSession session) {
        Member loginUser = (Member) session.getAttribute("loginUser");
        return ResponseEntity.ok(memberService.getBoards(loginUser.getEmail()));
    }
}

