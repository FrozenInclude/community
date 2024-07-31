package com.bcsd.community.entity;

import com.bcsd.community.controller.dto.request.ArticleUpdateRequestDto;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "article")
public class Article {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "article_id")
    private Long id;

    private String title;

    private String content;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @ManyToOne
    @JoinColumn(name="board_id")
    private Board board;

    @ManyToOne
    @JoinColumn(name="member_id")
    private Member member;

    @OneToMany(mappedBy = "classArticle",cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comment> comments;

    public void update(ArticleUpdateRequestDto dto) {
        if (dto.title()!= null) {
            this.title = dto.title();
        }
        if (dto.content() != null) {
            this.content=dto.content();
        }
        this.updatedAt = LocalDateTime.now();
    }
}
