package com.bcsd.community.entity;

import com.bcsd.community.controller.dto.request.MemberUpdateRequestDto;
import com.bcsd.community.util.PasswordUtils;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "member")
public class Member {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Long id;

    private String username;

    private String email;

    private String password;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "member",cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Article> articles;

    private String salt;

    public void update(MemberUpdateRequestDto dto) {
        if (dto.username() != null) {
            this.username = dto.username();
        }
        if (dto.password() != null) {
            salt = PasswordUtils.generateSalt();
            this.password = PasswordUtils.hashPasswordWithSalt(dto.password(), salt);
        }
        this.updatedAt = LocalDateTime.now();
    }

}