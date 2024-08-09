package com.bcsd.community.util;

import com.bcsd.community.entity.Article;
import com.bcsd.community.entity.Comment;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import org.springframework.data.jpa.domain.Specification;

public class CommentSpecification {

    public static Specification<Comment> hasKeywordInTitleOrContent(String keyword) {
        return (Root<Comment> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) -> {
            if (keyword == null || keyword.isEmpty()) {
                return criteriaBuilder.conjunction();
            }
            String pattern = "%" + keyword + "%";
            return criteriaBuilder.like(root.get("content"), pattern);
        };
    }

    public static Specification<Comment> hasAuthor(String author) {
        return (root, query, criteriaBuilder) -> {
            if (author == null || author.isEmpty()) {
                return criteriaBuilder.conjunction();
            }
            String pattern = "%" + author + "%";
            return criteriaBuilder.like(root.get("commentAuthor").get("username"), pattern);
        };
    }

    public static Specification<Comment> hasArticleId(Long boardId) {
        return (root, query, criteriaBuilder) -> {
            if (boardId == null) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.equal(root.get("classArticle").get("id"), boardId);
        };
    }

    public static Specification<Comment> hasMemberId(Long memberId) {
        return (root, query, criteriaBuilder) -> {
            if (memberId == null) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.equal(root.get("commentAuthor").get("id"), memberId);
        };
    }

}
