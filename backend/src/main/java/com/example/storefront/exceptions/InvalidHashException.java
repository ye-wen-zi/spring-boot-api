package com.example.storefront.exceptions;

public class InvalidHashException extends RuntimeException {
    public InvalidHashException(String message) {
        super(message);
    }
}
