package com.sample.order.exception;

import org.springframework.http.HttpStatus;

public class AddressNotFoundException extends RuntimeException{
    private HttpStatus status;
    public AddressNotFoundException(String message, HttpStatus status) {
        super(message);
        this.status = status;
    }
    public HttpStatus getStatus() {
        return status;
    }
}
