package com.bcsd.community.interceptor;

import com.bcsd.community.entity.Member;
import com.bcsd.community.service.ArticleService;
import com.bcsd.community.service.BoardService;
import com.bcsd.community.service.CommentService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.Objects;

@Component
@AllArgsConstructor
public class SessionInterceptor implements HandlerInterceptor {

    private final BoardService boardService;
    private final ArticleService articleService;
    private final CommentService commentService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String method = request.getMethod();
        String requestURI = request.getRequestURI();

        // /api/member 경로의 POST 요청은 인증 없이 허용
        if (requestURI.startsWith("/api/member") && "POST".equalsIgnoreCase(method)) {
            return true;
        }

        // /api/member 경로이거나 POST 요청일 때 로그인 체크
        if (requestURI.startsWith("/api/member") || "POST".equalsIgnoreCase(method)) {
            HttpSession session = request.getSession(false);
            if (session == null || session.getAttribute("loginUser") == null) {
                response.setContentType("text/plain; charset=UTF-8");
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.getWriter().write("로그인이 필요합니다");
                return false;
            }
        }

        // 인증된 사용자일 때 권한 체크
        if (!isAuthorized(request)) {
            response.setContentType("text/plain; charset=UTF-8");
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            response.getWriter().write("잘못된 접근입니다");
            return false;
        }

        return true;
    }

    private boolean isAuthorized(HttpServletRequest request) {
        HttpSession session = request.getSession(false);

        String method = request.getMethod();
        String requestURI = request.getRequestURI();

        if ("GET".equalsIgnoreCase(method)) {
            return true;
        } else if ("PUT".equalsIgnoreCase(method) || "DELETE".equalsIgnoreCase(method)) {
            if (session == null || session.getAttribute("loginUser") == null) {
                return false;
            }

            Member loginUser = (Member) session.getAttribute("loginUser");
            String[] uriParts = requestURI.split("/");

            if (requestURI.startsWith("/api/board/")) {
                try {
                    Long boardId = Long.parseLong(uriParts[uriParts.length - 1]);
                    Long authorId = boardService.getAuthor(boardId).id();
                    return Objects.equals(authorId, loginUser.getId());
                } catch (NumberFormatException e) {
                    return false;
                }
            } else if (requestURI.startsWith("/api/article/")) {
                try {
                    Long articleId = Long.parseLong(uriParts[uriParts.length - 1]);
                    Long authorId = articleService.getAuthor(articleId).id();
                    return Objects.equals(authorId, loginUser.getId());
                } catch (NumberFormatException e) {
                    return false;
                }
            } else if (requestURI.startsWith("/api/comment/")) {
                try {
                    Long commentId = Long.parseLong(uriParts[uriParts.length - 1]);
                    Long authorId = commentService.getAuthor(commentId).id();
                    return Objects.equals(authorId, loginUser.getId());
                } catch (NumberFormatException e) {
                    return false;
                }
            }
        }

        return true;
    }
}