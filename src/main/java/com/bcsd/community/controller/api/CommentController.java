package com.bcsd.community.controller.api;

import com.bcsd.community.controller.dto.request.CommentWriteRequestDto;
import com.bcsd.community.entity.Member;
import com.bcsd.community.service.CommentService;
import jakarta.servlet.http.HttpSession;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@AllArgsConstructor
@RestController
@RequestMapping("/api/comment")
public class CommentController {

    private final CommentService commentService;

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable Long id) {
        return ResponseEntity.ok(commentService.findById(id));
    }

    @GetMapping
    public ResponseEntity<?> getAll() {
        return ResponseEntity.ok(commentService.findAll());
    }


    @GetMapping("/{id}/author")
    public ResponseEntity<?> getAuthor(@PathVariable Long id) {
        return ResponseEntity.ok(commentService.getAuthor(id));
    }

    @GetMapping("/{id}/article")
    public ResponseEntity<?> getArticle(@PathVariable Long id) {
        return ResponseEntity.ok(commentService.getArticle(id));
    }

    @PostMapping
    public ResponseEntity<?> write(
            @Validated @RequestBody CommentWriteRequestDto request,
            HttpSession session
    ) {
        Member loginUser = (Member) session.getAttribute("loginUser");
        return ResponseEntity.ok(commentService.write(loginUser, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id,
                                    HttpSession session
    ) {
        Member loginUser = (Member) session.getAttribute("loginUser");
        commentService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
