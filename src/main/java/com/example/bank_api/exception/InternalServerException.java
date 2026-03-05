package com.example.bank_api.exception;

public class InternalServerException extends RuntimeException {
    public InternalServerException(String message) {
        super("Internal server error " + message);
        int errorCode = 500;
    }
}
