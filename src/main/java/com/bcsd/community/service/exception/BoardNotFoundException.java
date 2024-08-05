package com.bcsd.community.service.exception;

public class BoardNotFoundException extends RuntimeException {
    public BoardNotFoundException() {
        super("존재하지 않는 게시판 입니다");
    }
}
