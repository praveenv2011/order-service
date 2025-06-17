package com.sample.order.exception;

import org.springframework.http.HttpStatus;

public class OrderItemAccessException extends RuntimeException {
    private HttpStatus httpStatus;
    public OrderItemAccessException(String string, HttpStatus httpStatus) {
        super(string);
        this.httpStatus = httpStatus;
    }
    public HttpStatus getHttpStatus() {
        return httpStatus;
    }
}
