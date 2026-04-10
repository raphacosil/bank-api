package com.example.bank_api.unit.config;

import com.example.bank_api.config.GlobalExceptionHandler;
import com.example.bank_api.exception.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.Assertions.assertThat;

class GlobalExceptionHandlerTest {

    private GlobalExceptionHandler handler;

    @BeforeEach
    void setUp() {
        handler = new GlobalExceptionHandler();
    }

    @Test
    void shouldHandleNotFoundException() {
        NotFoundException exception = new NotFoundException();

        ResponseEntity<Object> response = handler.handleNotFoundException(exception);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(response.getBody()).isEqualTo("Resource not found");
    }

    @Test
    void shouldHandleBadRequestException() {
        BadRequestException exception = new BadRequestException("");

        ResponseEntity<Object> response = handler.handleBadRequestException(exception);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).isEqualTo("Bad request ");
    }

    @Test
    void shouldHandleInternalServerException() {
        InternalServerException exception = new InternalServerException("");

        ResponseEntity<Object> response = handler.handleInternalServerException(exception);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
        assertThat(response.getBody()).isEqualTo("Internal server error ");
    }

    @Test
    void shouldHandleUnprocessableEntityException() {
        UnprocessableEntityException exception =
                new UnprocessableEntityException("");

        ResponseEntity<Object> response =
                handler.handleUnprocessableEntityException(exception);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNPROCESSABLE_ENTITY);
        assertThat(response.getBody()).isEqualTo("Unprocessable entity ");
    }

    @Test
    void shouldHandleGenericException() {
        Exception exception = new Exception("Unexpected error");

        ResponseEntity<Object> response = handler.handleException(exception);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
        assertThat(response.getBody()).isEqualTo("Unexpected error");
    }
}

