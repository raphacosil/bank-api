package com.example.bank_api.config.exception;

public class BadRequestException extends RuntimeException {
  public BadRequestException(String message) {
    super("Bad request " + message);
    int errorCode = 400;
  }
}
