package com.example.bank_api.unit.service;

import com.example.bank_api.exception.NotFoundException;
import com.example.bank_api.model.Balance;
import com.example.bank_api.repository.BalanceRepository;
import com.example.bank_api.service.BalanceService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class BalanceServiceTest {

    @Mock
    BalanceRepository balanceRepository;

    @InjectMocks
    BalanceService balanceService;

    @Test
    void whenGetBalanceByCustomer_thenReturnBalance(){
        when(balanceRepository.findByCustomerId(1L)).thenReturn(Optional.of(new Balance()));

        balanceService.getBalanceByCustomer(1L);

        verify(balanceRepository, times(1)).findByCustomerId(1L);
    }

    @Test
    void whenGetBalanceByCustomer_thenThrowNotFoundException(){
        when(balanceRepository.findByCustomerId(1L)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> balanceService.getBalanceByCustomer(1L));

        verify(balanceRepository, times(1)).findByCustomerId(1L);
    }

    @Test
    void whenGetBalanceAmountByCustomer_thenReturnAmount() {
        when(balanceRepository.findAmountByCustomerId(1L)).thenReturn(Optional.of(100.0));

        balanceService.getBalanceAmountByCustomer(1L);

        verify(balanceRepository, times(1)).findAmountByCustomerId(1L);
    }

    @Test
    void whenGetBalanceAmountByCustomer_thenThrowNotFoundException(){
        when(balanceRepository.findAmountByCustomerId(1L)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> balanceService.getBalanceAmountByCustomer(1L));

        verify(balanceRepository, times(1)).findAmountByCustomerId(1L);
    }
}
