package com.bcsd.community.util;

import com.bcsd.community.entity.Article;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import org.springframework.data.jpa.domain.Specification;

public class ArticleSpecification {

    public static Specification<Article> hasKeywordInTitleOrContent(String keyword) {
        return (Root<Article> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) -> {
            if (keyword == null || keyword.isEmpty()) {
                return criteriaBuilder.conjunction();
            }
            String pattern = "%" + keyword + "%";
            return criteriaBuilder.or(
                    criteriaBuilder.like(root.get("title"), pattern),
                    criteriaBuilder.like(root.get("content"), pattern)
            );
        };
    }

    public static Specification<Article> hasAuthor(String author) {
        return (root, query, criteriaBuilder) -> {
            if (author == null || author.isEmpty()) {
                return criteriaBuilder.conjunction();
            }
            String pattern = "%" + author + "%";
            return criteriaBuilder.like(root.get("member").get("username"), pattern);
        };
    }

    public static Specification<Article> hasBoardId(Long boardId) {
        return (root, query, criteriaBuilder) -> {
            if (boardId == null) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.equal(root.get("board").get("id"), boardId);
        };
    }

    public static Specification<Article> hasMemberId(Long memberId) {
        return (root, query, criteriaBuilder) -> {
            if (memberId == null) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.equal(root.get("member").get("id"), memberId);
        };
    }

}