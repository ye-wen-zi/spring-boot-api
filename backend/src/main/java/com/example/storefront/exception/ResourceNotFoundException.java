package com.example.storefront.exception;

import org.springframework.http.HttpStatus;

import lombok.Getter;

public class ResourceNotFoundException extends RuntimeException {

    @Getter()
    private final HttpStatus status;

    public ResourceNotFoundException(String message) {
        super(message);
        this.status = HttpStatus.NOT_FOUND;
    }

}
