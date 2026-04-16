package com.example.bank_api.config.exception;

public class NotFoundException extends RuntimeException {
    public NotFoundException() {
        super("Resource not found");
        int errorCode = 404;
    }
}
