package com.sample.order.exception;

import com.sample.order.response.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionalHandler {


    @ExceptionHandler(CartNotFoundException.class)
    public ResponseEntity<ErrorResponse> CartNotFoundExceptionHandler(CartNotFoundException e){
        ErrorResponse errorResponse = new ErrorResponse(e.getStatus().value(),e.getMessage());
        return ResponseEntity.status(e.getStatus()).body(errorResponse);
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ErrorResponse> UserNotFoundExceptionHandler(UserNotFoundException e){
        ErrorResponse errorResponse = new ErrorResponse(e.getStatus().value(),e.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
    }


    @ExceptionHandler(AddressNotFoundException.class)
    public ResponseEntity<ErrorResponse> AddressNotFoundExceptionHandler(AddressNotFoundException e){
        ErrorResponse errorResponse = new ErrorResponse(e.getStatus().value(), e.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
    }

    @ExceptionHandler(OrderAccessException.class)
    public ResponseEntity<ErrorResponse> OrderAccessExceptionHandler(OrderAccessException e){
        ErrorResponse errorResponse = new ErrorResponse(e.getHttpStatus().value(), e.getMessage());
        return ResponseEntity.status(e.getHttpStatus()).body(errorResponse);
    }

    @ExceptionHandler(OrderItemAccessException.class)
    public ResponseEntity<ErrorResponse> OrderItemAccessExceptionHandler(OrderItemAccessException e){
        ErrorResponse errorResponse = new ErrorResponse(e.getHttpStatus().value(), e.getMessage());
        return ResponseEntity.status(e.getHttpStatus()).body(errorResponse);
    }

    @ExceptionHandler(CartAccessException.class)
    public ResponseEntity<ErrorResponse> CartAccessExceptionHandler(CartAccessException e) {
        ErrorResponse errorResponse = new ErrorResponse(e.getHttpStatus().value(), e.getMessage());
        return ResponseEntity.status(e.getHttpStatus()).body(errorResponse);
    }
    @ExceptionHandler(UserAccessException.class)
    public ResponseEntity<ErrorResponse> UserAccessExceptionHandler(UserAccessException e) {
        ErrorResponse errorResponse = new ErrorResponse(e.getHttpStatus().value(), e.getMessage());
        return ResponseEntity.status(e.getHttpStatus()).body(errorResponse);
    }

    @ExceptionHandler(AddressAccessException.class)
    public ResponseEntity<ErrorResponse> AddressAccessExceptionHandler(AddressAccessException e) {
        ErrorResponse errorResponse = new ErrorResponse(e.getStatus().value(), e.getMessage());
        return ResponseEntity.status(e.getStatus()).body(errorResponse);
    }

}
