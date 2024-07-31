package com.bcsd.community.service.exception;

public class AlreadyExistsException extends RuntimeException {
    public AlreadyExistsException(String fieldName) {
        super(fieldName);
    }
}
