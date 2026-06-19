package com.example.storefront.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice()
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorDetails> handleResourceNotfound(ResourceNotFoundException ex) {
        ErrorDetails errorDetails = new ErrorDetails(
                ex.getStatus().value(),
                ex.getStatus().getReasonPhrase(),
                ex.getMessage());

        return new ResponseEntity<>(errorDetails, ex.getStatus());
    }

    public ResponseEntity<ErrorDetails> handleGlobalException(Exception ex) {
        ErrorDetails errorDetails = new ErrorDetails(HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "Internal Server Exception", "Something went wrong!");

                return ResponseEntity.internalServerError().body(errorDetails);
    }
}
