package com.example.bank_api.controller;


import com.example.bank_api.model.Transference;
import com.example.bank_api.service.TransferenceService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;

import java.sql.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class TransferenceControllerTest {

    @Mock
    TransferenceService transferenceService;

    @InjectMocks
    TransferenceController transferenceController;

    @Mock
    BindingResult bindingResult;

    Transference transference;

    @BeforeEach
    void setUp() {
        transference = new Transference(
                1L,
                2L,
                3l,
                100.00,
                new Date(2000, 1, 1)
        );
    }

    @Test
    void whenFindById_shouldReturn200() {
        when(transferenceService.findById(1L)).thenReturn(transference);

        ResponseEntity<Transference> response = transferenceController.findById(1L);

        assertNotNull(response.getBody());
        assertEquals(1L, response.getBody().getId());
        assertEquals(2L, response.getBody().getPayer());

        assertEquals(3L, response.getBody().getPayee());
        assertEquals(100.00, response.getBody().getValue());
        assertEquals(new Date(2000, 1, 1), response.getBody().getCreatedAt());
    }

    @Test
    void whenFindAll_shouldReturn200() {
        when(transferenceService.findAll()).thenReturn(List.of(transference));

        ResponseEntity<List<Transference>> response = transferenceController.findAll();

        assertNotNull(response.getBody());
        assertEquals(1L, response.getBody().getFirst().getId());
        assertEquals(2L, response.getBody().getFirst().getPayer());
        assertEquals(3L, response.getBody().getFirst().getPayee());
        assertEquals(100.00, response.getBody().getFirst().getValue());
        assertEquals(new Date(2000, 1, 1), response.getBody().getFirst().getCreatedAt());
    }

    @Test
    void whenFindByCustomer_shouldReturn200() {
        when(transferenceService.findByCustomer(2L)).thenReturn(List.of(transference));

        ResponseEntity<List<Transference>> response = transferenceController.findByCustomer(2L);

        assertNotNull(response.getBody());
        assertEquals(1L, response.getBody().getFirst().getId());
        assertEquals(2L, response.getBody().getFirst().getPayer());
        assertEquals(3L, response.getBody().getFirst().getPayee());
        assertEquals(100.00, response.getBody().getFirst().getValue());
        assertEquals(new Date(2000, 1, 1), response.getBody().getFirst().getCreatedAt());
    }

    @Test
    void whenFindBetweenCustomers_shouldReturn200() {
        when(transferenceService.findBetweenCustomers(2L, 3L)).thenReturn(List.of(transference));

        ResponseEntity<List<Transference>> response = transferenceController.findBetweenCustomers(2L, 3L);

        assertNotNull(response.getBody());
        assertEquals(1L, response.getBody().getFirst().getId());
        assertEquals(2L, response.getBody().getFirst().getPayer());
        assertEquals(3L, response.getBody().getFirst().getPayee());
        assertEquals(100.00, response.getBody().getFirst().getValue());
        assertEquals(new Date(2000, 1, 1), response.getBody().getFirst().getCreatedAt());
    }

    @Test
    void whenRefund_shouldReturn200() {
        doNothing().when(transferenceService).refund(1L);

        ResponseEntity<Void> response = transferenceController.refund(1L);

        assertEquals(HttpStatusCode.valueOf(200), response.getStatusCode());
    }

    @Test
    void whenTransfer_shouldReturn200() {
        doNothing().when(transferenceService).transfer(transference);

        ResponseEntity<Void> response = transferenceController.transfer(transference, bindingResult);

        assertEquals(HttpStatusCode.valueOf(200), response.getStatusCode());
    }
}
