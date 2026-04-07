package com.example.bank_api.unit.controller;

import com.example.bank_api.model.Balance;
import com.example.bank_api.unit.service.BalanceService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class BalanceControllerTest {

    @Mock
    BalanceService balanceService;

    @InjectMocks
    BalanceController balanceController;

    Balance balance;

    @BeforeEach
    void setUp() {
        balance = new Balance(
                1L,
                2L,
                100.00
        );
    }

    @Test
    public void whenFindById_ShouldReturnBalance() {
        when(balanceService.getBalanceByCustomer(2L)).thenReturn(balance);

        ResponseEntity<Balance> response = balanceController.findById(2L);

        assertNotNull(response.getBody());
        assertEquals(1L, response.getBody().getId());
        assertEquals(2L, response.getBody().getCustomerId());
        assertEquals(100.00, response.getBody().getAmount());
        verify(balanceService, times(1)).getBalanceByCustomer(2L);
    }

    @Test
    public void whenFindAmountById_ShouldReturnBalanceAmount() {
        when(balanceService.getBalanceAmountByCustomer(2L)).thenReturn(100.00);

        ResponseEntity<Double> response = balanceController.findAmountById(2L);

        assertNotNull(response.getBody());
        assertEquals(100.00, response.getBody());
        verify(balanceService, times(1)).getBalanceAmountByCustomer(2L);
    }
}
