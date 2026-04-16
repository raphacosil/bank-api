package com.example.bank_api.config.exception;

public class UnprocessableEntityException extends RuntimeException {
    public UnprocessableEntityException(String message) {
        super("Unprocessable entity " + message);
        int errorCode = 422;
    }
}
