package com.example.bank_api.unit.use_case.transference;

import com.example.bank_api.config.exception.NotFoundException;
import com.example.bank_api.config.exception.UnprocessableEntityException;
import com.example.bank_api.domain.model.Balance;
import com.example.bank_api.domain.model.Transference;
import com.example.bank_api.domain.use_case.transference.RefundUseCase;
import com.example.bank_api.infra.repository.BalanceRepository;
import com.example.bank_api.infra.repository.TransferenceRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class RefundUseCaseTest {

    @Mock
    TransferenceRepository transferenceRepository;
    @Mock
    BalanceRepository balanceRepository;

    @InjectMocks
    RefundUseCase refundUseCase;

    @Test
    void whenExecuteAndTransferenceEmpty_thenThrowNotFoundException() {
        when(transferenceRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> refundUseCase.execute(1L));

        verify(balanceRepository, times(0)).save(any(Balance.class));
        verify(transferenceRepository, times(0)).delete(any(Transference.class));
    }

    @Test
    void whenExecuteAndPayerBalanceIsNotFound_thenThrowNotFoundException() {
        Transference transference = new Transference(1L, 1L, 2L, 100.00, null);
        Optional<Transference> transferenceOptional = Optional.of(transference);
        when(transferenceRepository.findById(1L)).thenReturn(transferenceOptional);
        when(balanceRepository.findByCustomerId(1L)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> refundUseCase.execute(1L));

        verify(balanceRepository, times(0)).save(any(Balance.class));
        verify(transferenceRepository, times(0)).delete(any(Transference.class));
    }

    @Test
    void whenExecuteAndPayeeBalanceIsNotFound_thenThrowNotFoundException() {
        Transference transference = new Transference(1L, 1L, 2L, 100.00, null);
        Optional<Transference> transferenceOptional = Optional.of(transference);
        Balance balance = new Balance(1L, 1L, 100.00);

        when(transferenceRepository.findById(1L)).thenReturn(transferenceOptional);
        Optional<Balance> balanceOptional = Optional.of(balance);
        when(balanceRepository.findByCustomerId(1L)).thenReturn(balanceOptional);
        when(balanceRepository.findByCustomerId(2L)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> refundUseCase.execute(1L));

        verify(balanceRepository, times(0)).save(any(Balance.class));
        verify(transferenceRepository, times(0)).delete(any(Transference.class));
    }

    @Test
    void whenExecuteAndNotEnoughBalance_thenThrowUnprocessableEntityException() {
        Transference transference = new Transference(1L, 1L, 2L, 150.00, null);
        Balance balance = new Balance(1L, 1L, 100.00);
        Optional<Balance> balanceOptional = Optional.of(balance);
        Optional<Transference> transferenceOptional = Optional.of(transference);

        when(transferenceRepository.findById(1L)).thenReturn(transferenceOptional);
        when(balanceRepository.findByCustomerId(1L)).thenReturn(balanceOptional);
        when(balanceRepository.findByCustomerId(2L)).thenReturn(balanceOptional);

        UnprocessableEntityException exception = assertThrows(UnprocessableEntityException.class, () -> refundUseCase.execute(1L));

        verify(balanceRepository, times(0)).save(any(Balance.class));
        verify(transferenceRepository, times(0)).delete(any(Transference.class));
        assertEquals("Unprocessable entity Not enough balance", exception.getMessage());
    }

    @Test
    void whenExecute_thenReturnVoid() {
        Transference transference = new Transference(1L, 1L, 2L, 100.00, null);
        Optional<Transference> transferenceOptional = Optional.of(transference);
        Balance balance = new Balance(1L, 1L, 100.00);
        Optional<Balance> balanceOptional = Optional.of(balance);

        when(transferenceRepository.findById(1L)).thenReturn(transferenceOptional);
        when(balanceRepository.findByCustomerId(1L)).thenReturn(balanceOptional);
        when(balanceRepository.findByCustomerId(2L)).thenReturn(balanceOptional);
        when(balanceRepository.save(any())).thenReturn(balance);

        refundUseCase.execute(1L);

        verify(balanceRepository, times(2)).findByCustomerId(any(Long.class));
        verify(balanceRepository, times(2)).save(any(Balance.class));
        verify(transferenceRepository, times(1)).delete(any(Transference.class));
    }
}
