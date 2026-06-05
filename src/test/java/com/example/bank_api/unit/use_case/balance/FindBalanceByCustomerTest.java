package com.example.bank_api.unit.use_case.balance;

import com.example.bank_api.config.exception.NotFoundException;
import com.example.bank_api.domain.use_case.balance.FindBalanceByCustomer;
import com.example.bank_api.infra.repository.BalanceRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
public class FindBalanceByCustomerTest {

    @Mock
    BalanceRepository balanceRepository;

    @InjectMocks
    FindBalanceByCustomer findBalanceByCustomer;

    @Test
    void whenGetBalanceAmountByCustomer_thenReturnAmount() {
        when(balanceRepository.findAmountByCustomerId(1L)).thenReturn(Optional.of(100.0));

        findBalanceByCustomer.execute(1L);

        verify(balanceRepository, times(1)).findAmountByCustomerId(1L);
    }

    @Test
    void whenGetBalanceAmountByCustomer_thenThrowNotFoundException(){
        when(balanceRepository.findAmountByCustomerId(1L)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> findBalanceByCustomer.execute(1L));

        verify(balanceRepository, times(1)).findAmountByCustomerId(1L);
    }
}
