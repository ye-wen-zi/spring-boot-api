package com.example.storefront.exceptions;

import org.springframework.http.HttpStatus;

import lombok.Getter;

public class ResourceNotFoundException extends RuntimeException {

    @Getter()
    private final HttpStatus status;

    ResourceNotFoundException(String message) {
        super(message);
        this.status = HttpStatus.NOT_FOUND;
    }

}
