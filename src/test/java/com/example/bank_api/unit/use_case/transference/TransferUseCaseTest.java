package com.example.bank_api.unit.use_case.transference;

import com.example.bank_api.config.exception.NotFoundException;
import com.example.bank_api.config.exception.UnprocessableEntityException;
import com.example.bank_api.domain.model.Balance;
import com.example.bank_api.domain.model.Transference;
import com.example.bank_api.domain.use_case.transference.TransferUseCase;
import com.example.bank_api.infra.gateway.ApiGateway;
import com.example.bank_api.infra.gateway.dto.AuthorizeResponse;
import com.example.bank_api.infra.gateway.dto.AuthorizeResponseData;
import com.example.bank_api.infra.gateway.dto.SendNotificationResponse;
import com.example.bank_api.infra.repository.BalanceRepository;
import com.example.bank_api.infra.repository.CustomerRepository;
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
public class TransferUseCaseTest {

    @Mock
    TransferenceRepository transferenceRepository;
    @Mock
    BalanceRepository balanceRepository;
    @Mock
    CustomerRepository customerRepository;
    @Mock
    ApiGateway apiGateway;

    @InjectMocks
    TransferUseCase transferUseCase;

    @Test
    void whenExecuteAndPayerEqualPayee_thenThrowUnprocessableEntityException() {
        Transference transference = new Transference(1L, 1L, 1L, 100.00, null);

        UnprocessableEntityException exception = assertThrows(UnprocessableEntityException.class, () -> transferUseCase.execute(transference));

        verify(balanceRepository, times(0)).save(any(Balance.class));
        verify(transferenceRepository, times(0)).save(any(Transference.class));
        assertEquals("Unprocessable entity Payer and payee should be different", exception.getMessage());
    }

    @Test
    void whenExecuteAndPayerIsBusiness_thenThrowUnprocessableEntityException() {
        Transference transference = new Transference(1L, 1L, 2L, 100.00, null);
        when(customerRepository.findIsBusinessById(1L)).thenReturn(true);

        UnprocessableEntityException exception = assertThrows(UnprocessableEntityException.class, () -> transferUseCase.execute(transference));

        verify(balanceRepository, times(0)).save(any(Balance.class));
        verify(transferenceRepository, times(0)).save(any(Transference.class));
        assertEquals("Unprocessable entity Payer should not be a business", exception.getMessage());
    }

    @Test
    void whenExecuteAndPayerBalanceIsNotFound_thenThrowNotFoundException() {
        Transference transference = new Transference(1L, 1L, 2L, 100.00, null);

        when(customerRepository.findIsBusinessById(1L)).thenReturn(false);
        when(balanceRepository.findByCustomerId(1L)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> transferUseCase.execute(transference));

        verify(balanceRepository, times(0)).save(any(Balance.class));
        verify(transferenceRepository, times(0)).save(any(Transference.class));
    }

    @Test
    void whenExecuteAndPayeeBalanceIsNotFound_thenThrowNotFoundException() {
        Transference transference = new Transference(1L, 1L, 2L, 100.00, null);
        Balance balance = new Balance(1L, 1L, 100.00);
        Optional<Balance> balanceOptional = Optional.of(balance);

        when(customerRepository.findIsBusinessById(1L)).thenReturn(false);
        when(balanceRepository.findByCustomerId(1L)).thenReturn(balanceOptional);
        when(balanceRepository.findByCustomerId(2L)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> transferUseCase.execute(transference));

        verify(balanceRepository, times(0)).save(any(Balance.class));
        verify(transferenceRepository, times(0)).save(any(Transference.class));
    }

    @Test
    void whenExecuteAndNotEnoughBalance_thenThrowUnprocessableEntityException() {
        Transference transference = new Transference(1L, 1L, 2L, 150.00, null);
        Balance balance = new Balance(1L, 1L, 100.00);
        Optional<Balance> balanceOptional = Optional.of(balance);

        when(customerRepository.findIsBusinessById(1L)).thenReturn(false);
        when(balanceRepository.findByCustomerId(1L)).thenReturn(balanceOptional);
        when(balanceRepository.findByCustomerId(2L)).thenReturn(balanceOptional);

        UnprocessableEntityException exception = assertThrows(UnprocessableEntityException.class, () -> transferUseCase.execute(transference));

        verify(balanceRepository, times(0)).save(any(Balance.class));
        verify(transferenceRepository, times(0)).save(any(Transference.class));
        assertEquals("Unprocessable entity Not enough balance", exception.getMessage());
    }

    @Test
    void whenExecute_thenReturnVoid() {
        Transference transference = new Transference(1L, 1L, 2L, 100.00, null);
        Balance balance = new Balance(1L, 1L, 100.00);
        Optional<Balance> balanceOptional = Optional.of(balance);

        when(balanceRepository.findByCustomerId(1L)).thenReturn(balanceOptional);
        when(balanceRepository.findByCustomerId(2L)).thenReturn(balanceOptional);
        when(balanceRepository.save(any())).thenReturn(balance);

        AuthorizeResponse authorizeResponse = new AuthorizeResponse(
                "success",
                new AuthorizeResponseData(true)
        );
        when(apiGateway.authorize()).thenReturn(authorizeResponse);

        SendNotificationResponse sendNotificationResponse = mock(SendNotificationResponse.class);
        when(sendNotificationResponse.getStatus()).thenReturn("success");
        when(apiGateway.sendNotification()).thenReturn(sendNotificationResponse);

        transferUseCase.execute(transference);

        verify(balanceRepository, times(2)).findByCustomerId(any(Long.class));
        verify(balanceRepository, times(2)).save(any(Balance.class));
        verify(transferenceRepository, times(1)).save(any(Transference.class));
        verify(apiGateway, times(1)).authorize();
        verify(apiGateway, times(1)).sendNotification();
    }

    @Test
    void whenExecuteAndAuthorizeFalse_thenRefund() {
        Transference transference = new Transference(1L, 1L, 2L, 100.00, null);
        Optional<Transference> transferenceOptional = Optional.of(transference);
        Balance balance = new Balance(1L, 1L, 100.00);
        Optional<Balance> balanceOptional = Optional.of(balance);

        when(balanceRepository.findByCustomerId(1L)).thenReturn(balanceOptional);
        when(balanceRepository.findByCustomerId(2L)).thenReturn(balanceOptional);
        when(balanceRepository.save(any())).thenReturn(balance);

        AuthorizeResponse authorizeResponse = new AuthorizeResponse("fail", new AuthorizeResponseData(false));
        when(apiGateway.authorize()).thenReturn(authorizeResponse);

        SendNotificationResponse sendNotificationResponse = mock(SendNotificationResponse.class);
        when(sendNotificationResponse.getStatus()).thenReturn("success");
        when(apiGateway.sendNotification()).thenReturn(sendNotificationResponse);

        when(transferenceRepository.save(transference)).thenReturn(transference);
        when(transferenceRepository.findById(1L)).thenReturn(transferenceOptional);

        transferUseCase.execute(transference);

        verify(balanceRepository, times(4)).findByCustomerId(any(Long.class));
        verify(balanceRepository, times(4)).save(any(Balance.class));
        verify(transferenceRepository, times(1)).save(any(Transference.class));
        verify(apiGateway, times(1)).authorize();
        verify(apiGateway, times(1)).sendNotification();
    }
}
