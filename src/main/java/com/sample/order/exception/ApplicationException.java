package com.sample.order.exception;

import org.springframework.http.HttpStatus;

public class ApplicationException extends Exception {
    private HttpStatus httpStatus;
    public ApplicationException(String string, HttpStatus httpStatus) {
        super(string);
        this.httpStatus = httpStatus;
    }
    public HttpStatus getHttpStatus() {
        return httpStatus;
    }
}
