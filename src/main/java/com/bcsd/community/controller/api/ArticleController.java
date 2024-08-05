package com.bcsd.community.controller.api;

import com.bcsd.community.controller.dto.request.ArticleUpdateRequestDto;
import com.bcsd.community.controller.dto.request.ArticleWriteRequestDto;
import com.bcsd.community.entity.Member;
import com.bcsd.community.service.ArticleService;
import jakarta.servlet.http.HttpSession;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@AllArgsConstructor
@RestController
@RequestMapping("/api/article")
public class ArticleController {

    private final ArticleService articleService;

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable Long id) {
        return ResponseEntity.ok(articleService.findById(id));
    }

    @GetMapping
    public ResponseEntity<?> getAll() {
        return ResponseEntity.ok(articleService.findAll());
    }

    @GetMapping("/{id}/comments")
    public ResponseEntity<?> getComments(@PathVariable Long id) {
        return ResponseEntity.ok(articleService.getComments(id));
    }

    @GetMapping("/{id}/author")
    public ResponseEntity<?> getAuthor(@PathVariable Long id) {
        return ResponseEntity.ok(articleService.getAuthor(id));
    }

    @GetMapping("/{id}/board")
    public ResponseEntity<?> getBoard(@PathVariable Long id) {
        return ResponseEntity.ok(articleService.getBoard(id));
    }

    @PostMapping
    public ResponseEntity<?> write(
            @Validated @RequestBody ArticleWriteRequestDto request,
            HttpSession session
    ) {
        Member loginUser = (Member) session.getAttribute("loginUser");
        return ResponseEntity.ok(articleService.write(loginUser, request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> edit(@PathVariable Long id,
                                  @Validated @RequestBody ArticleUpdateRequestDto request,
                                  HttpSession session
    ) {
        Member loginUser = (Member) session.getAttribute("loginUser");
        return ResponseEntity.ok(articleService.edit(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id,
                                    @Validated @RequestBody ArticleUpdateRequestDto request,
                                    HttpSession session
    ) {
        Member loginUser = (Member) session.getAttribute("loginUser");
        articleService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
