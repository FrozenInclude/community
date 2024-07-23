package com.bcsd.community.controller.restful;

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
        Member existingUser = (Member) session.getAttribute("loginUser");
        if (existingUser != null) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Already logged in");
        }

        Member user = memberService.login(request);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid username or password");
        }

        session.setAttribute("loginUser", user);
        return ResponseEntity.ok(user);
    }

    @PutMapping
    public ResponseEntity<?> updateMember(
            @Validated @RequestBody MemberUpdateRequestDto request,
            HttpSession session
    ) {
        Member loginUser = (Member) session.getAttribute("loginUser");
        if (loginUser == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Not logged in");
        }
        loginUser.update(request);
        MemberResponseDto response = MemberResponseDto.from(loginUser);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping
    public ResponseEntity<?> withdrawMember(
            HttpSession session
    ) {
        Member loginUser = (Member) session.getAttribute("loginUser");
        if (loginUser == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Not logged in");
        }
        memberService.withDraw(loginUser.getEmail());
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/info")
    public ResponseEntity<?> userInfo(HttpSession session) {
        Member loginUser = (Member) session.getAttribute("loginUser");
        if (loginUser == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Not logged in");
        }
        MemberResponseDto response = MemberResponseDto.from(loginUser);
        return ResponseEntity.ok(response);
    }

}

