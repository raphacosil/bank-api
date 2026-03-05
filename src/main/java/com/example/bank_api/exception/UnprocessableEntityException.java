package com.example.bank_api.exception;

public class UnprocessableEntityException extends RuntimeException {
    public UnprocessableEntityException(String message) {
        super("Unprocessable entity " + message);
        int errorCode = 422;
    }
}
