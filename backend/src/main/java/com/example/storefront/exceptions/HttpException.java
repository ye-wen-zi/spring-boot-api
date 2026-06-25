package com.example.storefront.exceptions;

import org.springframework.http.HttpStatus;

import lombok.Getter;

public class HttpException extends RuntimeException {

    @Getter
    private HttpStatus status;

    public HttpException(String message, HttpStatus status) {
        super(message);
        this.status = status;
    }
}
