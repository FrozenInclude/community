package com.bcsd.community.service.exception;

public class ArticleNotFoundException extends RuntimeException{
    public ArticleNotFoundException(){super("존재하지 않는 게시글 입니다");}
}
