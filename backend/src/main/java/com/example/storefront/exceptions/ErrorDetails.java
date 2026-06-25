package com.example.storefront.exceptions;

import java.time.LocalDateTime;
import java.util.Map;

import lombok.Data;

@Data()
public class ErrorDetails {
    private LocalDateTime timestamp;
    private int status;
    private Map<String, String> error;
    private String message;

    ErrorDetails(int status, Map<String, String> error, String message) {
        this.timestamp = LocalDateTime.now();
        this.status = status;
        this.error = error;
        this.message = message;
    }
}
