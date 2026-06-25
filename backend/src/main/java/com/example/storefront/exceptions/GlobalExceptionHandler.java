package com.example.storefront.exceptions;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

@RestControllerAdvice()
public class GlobalExceptionHandler {

    public <T extends HttpException> ErrorDetails createErrorDetails(T ex) {
        return new ErrorDetails(ex.getStatus().value(),
                /** ex.getStatus().getReasonPhrase() */
                new HashMap<>(), ex.getMessage());
    }

    @ExceptionHandler(HttpException.class)
    public ResponseEntity<ErrorDetails> handleResourceNotfound(HttpException ex) {
        return new ResponseEntity<>(this.createErrorDetails(ex), ex.getStatus());
    }

    @ExceptionHandler({ InvalidHashException.class, MethodArgumentTypeMismatchException.class })
    public ResponseEntity<ErrorDetails> handleInvalidHashids(Exception ex) {
        ErrorDetails errorDetails = new ErrorDetails(HttpStatus.BAD_REQUEST.value(), new HashMap<>(), "Invalid input");
        return ResponseEntity.badRequest().body(errorDetails);
    }

    @ExceptionHandler({ MethodArgumentNotValidException.class })
    public ResponseEntity<ErrorDetails> handleValidationException(MethodArgumentNotValidException ex) {
        Map<String, String> error = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach(err -> {
            error.put(((FieldError) err).getField(), err.getDefaultMessage());
        });
        return ResponseEntity.badRequest().body(
                new ErrorDetails(HttpStatus.BAD_REQUEST.value(), error, "Invalid input!"));
    }

    public ResponseEntity<ErrorDetails> handleGlobalException(Exception ex) {
        ErrorDetails errorDetails = new ErrorDetails(HttpStatus.INTERNAL_SERVER_ERROR.value(),
                new HashMap<>(), "Something went wrong!");

        return ResponseEntity.internalServerError().body(errorDetails);
    }
}
