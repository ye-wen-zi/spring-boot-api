package com.example.storefront.exceptions;

import org.springframework.http.HttpStatus;

public class ResourceNotFoundException extends HttpException {

    public ResourceNotFoundException(String message) {
        super(message, HttpStatus.NOT_FOUND);
    }

}
