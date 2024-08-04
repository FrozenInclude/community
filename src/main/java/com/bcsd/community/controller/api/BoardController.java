package com.bcsd.community.controller.api;

import com.bcsd.community.controller.dto.request.BoardCreateRequestDto;
import com.bcsd.community.controller.dto.request.BoardUpdateRequestDto;
import com.bcsd.community.entity.Member;
import com.bcsd.community.service.BoardService;
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
public class BoardController {

    private final BoardService boardService;

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable Long id) {
        return ResponseEntity.ok(boardService.findById(id));
    }

    @GetMapping
    public ResponseEntity<?> getAll() {
        return ResponseEntity.ok(boardService.findAll());
    }

    @GetMapping("/{id}/articles")
    public ResponseEntity<?> getArticles(@PathVariable Long id) {
        return ResponseEntity.ok(boardService.getArticles(id));
    }

    @GetMapping("/{id}/author")
    public ResponseEntity<?> getAuthor(@PathVariable Long id) {
        return ResponseEntity.ok(boardService.getAuthor(id));
    }

    @PostMapping
    public ResponseEntity<?> create(
            @Validated @RequestBody BoardCreateRequestDto request,
            HttpSession session
    ) {
        Member loginUser = (Member) session.getAttribute("loginUser");
        return ResponseEntity.ok(boardService.create(loginUser, request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> edit(@PathVariable Long id,
                                  @Validated @RequestBody BoardUpdateRequestDto request,
                                  HttpSession session
    ) {
        Member loginUser = (Member) session.getAttribute("loginUser");
        return ResponseEntity.ok(boardService.edit(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id,
                                    @Validated @RequestBody BoardCreateRequestDto request,
                                    HttpSession session
    ) {
        Member loginUser = (Member) session.getAttribute("loginUser");
        boardService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
