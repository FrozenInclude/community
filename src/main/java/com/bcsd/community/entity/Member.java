package com.bcsd.community.entity;

import com.bcsd.community.controller.dto.request.MemberUpdateRequestDto;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "member")
public class Member {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;

    private String email;

    private String password;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    private String salt;

    public void update(MemberUpdateRequestDto dto) {
        if (dto.username() != null) {
            this.username = dto.username();
        }
        if (dto.email() != null) {
            this.email = dto.email();
        }
        if (dto.password() != null) {
            this.password = dto.password();
        }
        this.updatedAt = LocalDateTime.now();
    }

}