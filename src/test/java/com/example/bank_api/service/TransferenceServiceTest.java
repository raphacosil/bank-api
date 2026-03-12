package com.example.bank_api.service;

import com.example.bank_api.exception.NotFoundException;
import com.example.bank_api.exception.UnprocessableEntityException;
import com.example.bank_api.gateway.ApiGateway;
import com.example.bank_api.model.Balance;
import com.example.bank_api.model.Transference;
import com.example.bank_api.repository.BalanceRepository;
import com.example.bank_api.repository.CustomerRepository;
import com.example.bank_api.repository.TransferenceRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TransferenceServiceTest {

    @Mock
    TransferenceRepository transferenceRepository;
    @Mock
    BalanceRepository balanceRepository;
    @Mock
    CustomerRepository customerRepository;
    @Mock
    ApiGateway apiGateway;

    @InjectMocks
    TransferenceService transferenceService;

    @Test
    void whenTransferAndPayerEqualPayee_thenThrowUnprocessableEntityException() {
        Transference transference = new Transference(1L, 1L, 1L, 100.00, null);

        UnprocessableEntityException exception = assertThrows(UnprocessableEntityException.class, () -> transferenceService.transfer(transference));

        verify(balanceRepository, times(0)).save(any(Balance.class));
        verify(transferenceRepository, times(0)).save(any(Transference.class));
        assertEquals("Unprocessable entity Payer and payee should be different", exception.getMessage());
    }

    @Test
    void whenTransferAndPayerIsBusiness_thenThrowUnprocessableEntityException() {
        Transference transference = new Transference(1L, 1L, 2L, 100.00, null);
        when(customerRepository.findIsBusinessById(1L)).thenReturn(true);

        UnprocessableEntityException exception = assertThrows(UnprocessableEntityException.class, () -> transferenceService.transfer(transference));

        verify(balanceRepository, times(0)).save(any(Balance.class));
        verify(transferenceRepository, times(0)).save(any(Transference.class));
        assertEquals("Unprocessable entity Payer should not be a business", exception.getMessage());
    }

    @Test
    void whenTransferAndPayerBalanceIsNotFound_thenThrowNotFoundException() {
        Transference transference = new Transference(1L, 1L, 2L, 100.00, null);

        when(customerRepository.findIsBusinessById(1L)).thenReturn(false);

        when(balanceRepository.findByCustomerId(1L)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> transferenceService.transfer(transference));

        verify(balanceRepository, times(0)).save(any(Balance.class));
        verify(transferenceRepository, times(0)).save(any(Transference.class));
    }

    @Test
    void whenTransferAndPayeeBalanceIsNotFound_thenThrowNotFoundException() {
        Transference transference = new Transference(1L, 1L, 2L, 100.00, null);
        Balance balance = new Balance(1L, 1L, 100.00);
        Optional<Balance> balanceOptional = Optional.of(balance);

        when(customerRepository.findIsBusinessById(1L)).thenReturn(false);

        when(balanceRepository.findByCustomerId(1L)).thenReturn(balanceOptional);
        when(balanceRepository.findByCustomerId(2L)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> transferenceService.transfer(transference));

        verify(balanceRepository, times(0)).save(any(Balance.class));
        verify(transferenceRepository, times(0)).save(any(Transference.class));
    }

    @Test
    void whenTransferAndNotEnoughBalance_thenThrowUnprocessableEntityException() {
        Transference transference = new Transference(1L, 1L, 2L, 150.00, null);
        Balance balance = new Balance(1L, 1L, 100.00);
        Optional<Balance> balanceOptional = Optional.of(balance);

        when(customerRepository.findIsBusinessById(1L)).thenReturn(false);

        when(balanceRepository.findByCustomerId(1L)).thenReturn(balanceOptional);
        when(balanceRepository.findByCustomerId(2L)).thenReturn(balanceOptional);

        UnprocessableEntityException exception = assertThrows(UnprocessableEntityException.class, () -> transferenceService.transfer(transference));

        verify(balanceRepository, times(0)).save(any(Balance.class));
        verify(transferenceRepository, times(0)).save(any(Transference.class));

        assertEquals("Unprocessable entity Not enough balance", exception.getMessage());
    }

    @Test
    void whenRefundAndTransferenceEmpty_thenThrowNotFoundException() {
        Transference transference = new Transference(1L, 1L, 2L, 100.00, null);
        when(transferenceRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> transferenceService.refund(1L));

        verify(balanceRepository, times(0)).save(any(Balance.class));
        verify(transferenceRepository, times(0)).delete(any(Transference.class));
    }


    @Test
    void whenUpdateAndPayerBalanceIsNotFound_thenThrowNotFoundException() {
        Transference transference = new Transference(1L, 1L, 2L, 100.00, null);
        Optional<Transference> transferenceOptional = Optional.of(transference);
        when(transferenceRepository.findById(1L)).thenReturn(transferenceOptional);

        when(balanceRepository.findByCustomerId(1L)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> transferenceService.refund(1L));

        verify(balanceRepository, times(0)).save(any(Balance.class));
        verify(transferenceRepository, times(0)).save(any(Transference.class));
    }

    @Test
    void whenUpdateAndPayeeBalanceIsNotFound_thenThrowNotFoundException() {
        Transference transference = new Transference(1L, 1L, 2L, 100.00, null);
        Optional<Transference> transferenceOptional = Optional.of(transference);

        Balance balance = new Balance(1L, 1L, 100.00);

        when(transferenceRepository.findById(1L)).thenReturn(transferenceOptional);

        Optional<Balance> balanceOptional = Optional.of(balance);

        when(balanceRepository.findByCustomerId(1L)).thenReturn(balanceOptional);
        when(balanceRepository.findByCustomerId(2L)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> transferenceService.refund(1L));

        verify(balanceRepository, times(0)).save(any(Balance.class));
        verify(transferenceRepository, times(0)).save(any(Transference.class));
    }

    @Test
    void whenUpdateAndNotEnoughBalance_thenThrowUnprocessableEntityException() {
        Transference transference = new Transference(1L, 1L, 2L, 150.00, null);
        Balance balance = new Balance(1L, 1L, 100.00);
        Optional<Balance> balanceOptional = Optional.of(balance);

        Optional<Transference> transferenceOptional = Optional.of(transference);

        when(transferenceRepository.findById(1L)).thenReturn(transferenceOptional);

        when(balanceRepository.findByCustomerId(1L)).thenReturn(balanceOptional);
        when(balanceRepository.findByCustomerId(2L)).thenReturn(balanceOptional);

        UnprocessableEntityException exception = assertThrows(UnprocessableEntityException.class, () -> transferenceService.refund(1L));

        verify(balanceRepository, times(0)).save(any(Balance.class));
        verify(transferenceRepository, times(0)).save(any(Transference.class));

        assertEquals("Unprocessable entity Not enough balance", exception.getMessage());
    }

    @Test
    void whenFindAll_thenReturnCustomerList() {
        Transference transference = new Transference(1L, 1L, 2L, 100.00, null);
        List<Transference> transferenceList = List.of(transference);
        when(transferenceRepository.findAll()).thenReturn(transferenceList);

        transferenceService.findAll();

        verify(transferenceRepository, times(1)).findAll();
    }

    @Test
    void whenFindByCustomer_thenReturnCustomerList() {
        Transference transference = new Transference(1L, 1L, 2L, 100.00, null);
        List<Transference> transferenceList = List.of(transference);
        when(transferenceRepository.findByCustomer(1L)).thenReturn(transferenceList);

        transferenceService.findByCustomer(1L);

        verify(transferenceRepository, times(1)).findByCustomer(1L);
    }

    @Test
    void whenFindBetweenCustomers_thenThrow() {
        Transference transference = new Transference(1L, 1L, 2L, 100.00, null);
        List<Transference> transferenceList = List.of(transference);

        assertThrows(UnprocessableEntityException.class, () -> transferenceService.findBetweenCustomers(1L, 1L));

        verify(transferenceRepository, times(0)).findByCustomer(any(), any());
    }

    @Test
    void whenFindBetweenCustomers_thenReturnCustomerList() {
        Transference transference = new Transference(1L, 1L, 2L, 100.00, null);
        List<Transference> transferenceList = List.of(transference);
        when(transferenceRepository.findByCustomer(1L, 2L)).thenReturn(transferenceList);

        transferenceService.findBetweenCustomers(1L, 2L);

        verify(transferenceRepository, times(1)).findByCustomer(1L, 2L);
    }

    @Test
    void whenFindById_thenReturnCustomer() {
        Transference transference = new Transference(1L, 1L, 2L, 100.00, null);
        when(transferenceRepository.findById(1L)).thenReturn(Optional.of(transference));

        transferenceService.findById(1L);

        verify(transferenceRepository, times(1)).findById(1L);
    }

    @Test
    void whenFindById_thenThrowNotFoundException(){
        when(transferenceRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> transferenceService.findById(1L));

        verify(transferenceRepository, times(1)).findById(1L);
    }
}
