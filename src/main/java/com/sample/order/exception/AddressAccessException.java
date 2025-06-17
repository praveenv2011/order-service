package com.sample.order.exception;

import org.springframework.http.HttpStatus;

public class AddressAccessException extends RuntimeException{
    private HttpStatus status;
    public AddressAccessException(String string, HttpStatus status) {
        super(string);
        this.status = status;
    }
    public HttpStatus getStatus() {
        return status;
    }
}
