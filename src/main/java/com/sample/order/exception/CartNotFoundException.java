package com.sample.order.exception;

import org.springframework.http.HttpStatus;

public class CartNotFoundException extends RuntimeException{
    private HttpStatus status;
    public CartNotFoundException(String message, HttpStatus status) {
        super(message);
        this.status = status;
    }

    public HttpStatus getStatus() {
        return status;
    }
}
