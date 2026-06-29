package com.example.storefront.exceptions;

import org.springframework.http.HttpStatus;

public class ConflictResourceException extends HttpException {

    public ConflictResourceException(String message) {
        super(message, HttpStatus.CONFLICT);
    }

}
