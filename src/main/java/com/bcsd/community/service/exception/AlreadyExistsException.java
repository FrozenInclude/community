package com.bcsd.community.service.exception;

public class EmailAlreadyExistsException extends RuntimeException {
    public EmailAlreadyExistsException(String fieldName) {
        super(fieldName);
    }
}
