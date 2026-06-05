package com.example.bank_api.unit.use_case.balance;

import com.example.bank_api.config.exception.NotFoundException;
import com.example.bank_api.domain.model.Balance;
import com.example.bank_api.domain.use_case.balance.FindBalanceAmountByCustomer;
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
public class FindBalanceAmountByCustomerTest {

    @Mock
    BalanceRepository balanceRepository;

    @InjectMocks
    FindBalanceAmountByCustomer findBalanceAmountByCustomer;

    @Test
    void whenGetBalanceByCustomer_thenReturnBalance(){
        when(balanceRepository.findByCustomerId(1L)).thenReturn(Optional.of(new Balance()));

        findBalanceAmountByCustomer.execute(1L);

        verify(balanceRepository, times(1)).findByCustomerId(1L);
    }

    @Test
    void whenGetBalanceByCustomer_thenThrowNotFoundException(){
        when(balanceRepository.findByCustomerId(1L)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> findBalanceAmountByCustomer.execute(1L));

        verify(balanceRepository, times(1)).findByCustomerId(1L);
    }
}
