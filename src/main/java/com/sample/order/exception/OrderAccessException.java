package com.sample.order.exception;

import org.springframework.http.HttpStatus;

public class OrderAccessException extends RuntimeException {
    private HttpStatus httpStatus;
    public OrderAccessException(String string, HttpStatus httpStatus) {
        super(string);
        this.httpStatus = httpStatus;
    }
    public HttpStatus getHttpStatus() {
        return httpStatus;
    }
}
