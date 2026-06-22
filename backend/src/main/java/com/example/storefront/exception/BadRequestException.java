package com.example.storefront.exception;

import org.springframework.http.HttpStatus;

import lombok.Getter;

public class BadRequestException extends RuntimeException {

    @Getter
    private final HttpStatus status;

    public BadRequestException(String message) {
        super(message);
        this.status = HttpStatus.BAD_REQUEST;
    }
}
