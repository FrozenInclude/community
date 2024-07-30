package com.bcsd.community.entity;

import com.bcsd.community.controller.dto.request.BoardUpdateRequestDto;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "board")
public class Board {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "board_id")
    private Long id;

    private String board_name;

    private String description;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "board",cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Article> articles;

    public void update(BoardUpdateRequestDto dto) {
        if (dto.board_name()!= null) {
            this.board_name = dto.board_name();
        }
        if (dto.description() != null) {
            this.description=dto.description();
        }
        this.updatedAt = LocalDateTime.now();
    }

}