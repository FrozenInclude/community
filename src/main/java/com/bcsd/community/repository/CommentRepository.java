package com.bcsd.community.repository;

import com.bcsd.community.entity.Article;
import com.bcsd.community.entity.Comment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    Page<Comment> findAll(Specification<Comment> spec, Pageable pageable);
}
