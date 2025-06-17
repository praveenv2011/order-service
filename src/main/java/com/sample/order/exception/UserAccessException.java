package com.sample.order.exception;

import org.springframework.http.HttpStatus;

public class UserAccessException extends RuntimeException {
    private final HttpStatus httpStatus;

    public UserAccessException(String string, HttpStatus httpStatus) {
        super(string);
        this.httpStatus = httpStatus;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }
}