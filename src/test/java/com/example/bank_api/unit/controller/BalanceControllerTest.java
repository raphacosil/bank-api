package com.example.bank_api.unit.controller;

import com.example.bank_api.boundary.controller.BalanceController;
import com.example.bank_api.domain.model.Balance;
import com.example.bank_api.domain.use_case.balance.FindBalanceAmountByCustomer;
import com.example.bank_api.domain.use_case.balance.FindBalanceByCustomer;
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
    FindBalanceByCustomer findBalanceByCustomer;

    @Mock
    FindBalanceAmountByCustomer findBalanceAmountByCustomer;

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
        when(findBalanceByCustomer.execute(2L)).thenReturn(balance);

        ResponseEntity<Balance> response = balanceController.findById(2L);

        assertNotNull(response.getBody());
        assertEquals(1L, response.getBody().getId());
        assertEquals(2L, response.getBody().getCustomerId());
        assertEquals(100.00, response.getBody().getAmount());
        verify(findBalanceAmountByCustomer, times(1)).execute(2L);
    }

    @Test
    public void whenFindAmountById_ShouldReturnBalanceAmount() {
        when(findBalanceAmountByCustomer.execute(2L)).thenReturn(100.00);

        ResponseEntity<Double> response = balanceController.findAmountById(2L);

        assertNotNull(response.getBody());
        assertEquals(100.00, response.getBody());
        verify(findBalanceAmountByCustomer, times(1)).execute(2L);
    }
}
