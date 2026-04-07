package com.example.bank_api.unit;
import com.example.bank_api.exception.BadRequestException;
import com.example.bank_api.exception.InternalServerException;
import com.example.bank_api.exception.NotFoundException;
import com.example.bank_api.exception.UnprocessableEntityException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;


public class ExceptionsTest {

    @Test
    void shouldThrowBadRequestException() {
        String message = "missing required field";

        BadRequestException exception = assertThrows(
                BadRequestException.class,
                () -> {
                    throw new BadRequestException(message);
                }
        );

        assertEquals("Bad request missing required field", exception.getMessage());
    }

    @Test
    void shouldThrowInternalServerException() {
        String message = "";

        InternalServerException exception = assertThrows(
                InternalServerException.class,
                () -> {
                    throw new InternalServerException(message);
                }
        );

        assertEquals("Internal server error ", exception.getMessage());
    }

    @Test
    void shouldThrowNotFoundException() {

        NotFoundException exception = assertThrows(
                NotFoundException.class,
                () -> {
                    throw new NotFoundException();
                }
        );

        assertEquals("Resource not found", exception.getMessage());
    }

    @Test
    void shouldThrowUnprocessableEntityException() {
        String message = "Payer should not be a business";

        UnprocessableEntityException exception = assertThrows(
                UnprocessableEntityException.class,
                () -> {
                    throw new UnprocessableEntityException(message);
                }
        );

        assertEquals("Unprocessable entity Payer should not be a business", exception.getMessage());
    }
}


