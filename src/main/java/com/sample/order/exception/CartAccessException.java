package com.sample.order.exception;

import org.springframework.http.HttpStatus;

public class CartAccessException extends RuntimeException {
    private HttpStatus httpStatus;
    public CartAccessException(String string, HttpStatus httpStatus) {
        super(string);
        this.httpStatus = httpStatus;
    }
    public HttpStatus getHttpStatus() {
        return httpStatus;
    }
}
