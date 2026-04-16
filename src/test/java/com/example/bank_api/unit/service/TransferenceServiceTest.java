package com.example.bank_api.unit.service;

import com.example.bank_api.config.exception.NotFoundException;
import com.example.bank_api.config.exception.UnprocessableEntityException;
import com.example.bank_api.infra.gateway.ApiGateway;
import com.example.bank_api.infra.gateway.dto.AuthorizeResponse;
import com.example.bank_api.infra.gateway.dto.AuthorizeResponseData;
import com.example.bank_api.infra.gateway.dto.SendNotificationResponse;
import com.example.bank_api.domain.model.Balance;
import com.example.bank_api.domain.model.Transference;
import com.example.bank_api.infra.repository.BalanceRepository;
import com.example.bank_api.infra.repository.CustomerRepository;
import com.example.bank_api.infra.repository.TransferenceRepository;
import com.example.bank_api.domain.service.TransferenceService;
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
        when(transferenceRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> transferenceService.refund(1L));

        verify(balanceRepository, times(0)).save(any(Balance.class));
        verify(transferenceRepository, times(0)).delete(any(Transference.class));
    }


    @Test
    void whenRefundAAndPayerBalanceIsNotFound_thenThrowNotFoundException() {
        Transference transference = new Transference(1L, 1L, 2L, 100.00, null);
        Optional<Transference> transferenceOptional = Optional.of(transference);
        when(transferenceRepository.findById(1L)).thenReturn(transferenceOptional);

        when(balanceRepository.findByCustomerId(1L)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> transferenceService.refund(1L));

        verify(balanceRepository, times(0)).save(any(Balance.class));
        verify(transferenceRepository, times(0)).delete(any(Transference.class));
    }

    @Test
    void whenRefundAAndPayeeBalanceIsNotFound_thenThrowNotFoundException() {
        Transference transference = new Transference(1L, 1L, 2L, 100.00, null);
        Optional<Transference> transferenceOptional = Optional.of(transference);

        Balance balance = new Balance(1L, 1L, 100.00);

        when(transferenceRepository.findById(1L)).thenReturn(transferenceOptional);

        Optional<Balance> balanceOptional = Optional.of(balance);

        when(balanceRepository.findByCustomerId(1L)).thenReturn(balanceOptional);
        when(balanceRepository.findByCustomerId(2L)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> transferenceService.refund(1L));

        verify(balanceRepository, times(0)).save(any(Balance.class));
        verify(transferenceRepository, times(0)).delete(any(Transference.class));
    }

    @Test
    void whenRefundAndNotEnoughBalance_thenThrowUnprocessableEntityException() {
        Transference transference = new Transference(1L, 1L, 2L, 150.00, null);
        Balance balance = new Balance(1L, 1L, 100.00);
        Optional<Balance> balanceOptional = Optional.of(balance);

        Optional<Transference> transferenceOptional = Optional.of(transference);

        when(transferenceRepository.findById(1L)).thenReturn(transferenceOptional);

        when(balanceRepository.findByCustomerId(1L)).thenReturn(balanceOptional);
        when(balanceRepository.findByCustomerId(2L)).thenReturn(balanceOptional);

        UnprocessableEntityException exception = assertThrows(UnprocessableEntityException.class, () -> transferenceService.refund(1L));

        verify(balanceRepository, times(0)).save(any(Balance.class));
        verify(transferenceRepository, times(0)).delete(any(Transference.class));

        assertEquals("Unprocessable entity Not enough balance", exception.getMessage());
    }

    @Test
    void whenTransfer_thenReturnVoid() {
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

        transferenceService.transfer(transference);

        verify(balanceRepository, times(2)).findByCustomerId(any(Long.class));
        verify(balanceRepository, times(2)).save(any(Balance.class));
        verify(transferenceRepository, times(1)).save(any(Transference.class));
        verify(apiGateway, times(1)).authorize();
        verify(apiGateway, times(1)).sendNotification();
    }

    @Test
    void whenTransferAndAuthorizeFalse_thenRefund() {
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


        transferenceService.transfer(transference);


        verify(balanceRepository, times(4)).findByCustomerId(any(Long.class));
        verify(balanceRepository, times(4)).save(any(Balance.class));
        verify(transferenceRepository, times(1)).save(any(Transference.class));
        verify(apiGateway, times(1)).authorize();
        verify(apiGateway, times(1)).sendNotification();
    }

    @Test
    void whenRefund_thenReturnVoid() {
        Transference transference = new Transference(1L, 1L, 2L, 100.00, null);
        Optional<Transference> transferenceOptional = Optional.of(transference);
        Balance balance = new Balance(1L, 1L, 100.00);
        Optional<Balance> balanceOptional = Optional.of(balance);

        when(transferenceRepository.findById(1L)).thenReturn(transferenceOptional);

        when(balanceRepository.findByCustomerId(1L)).thenReturn(balanceOptional);
        when(balanceRepository.findByCustomerId(2L)).thenReturn(balanceOptional);

        when(balanceRepository.save(any())).thenReturn(balance);

        transferenceService.refund(1L);

        verify(balanceRepository, times(2)).findByCustomerId(any(Long.class));
        verify(balanceRepository, times(2)).save(any(Balance.class));
        verify(transferenceRepository, times(1)).delete(any(Transference.class));
    }

    @Test
    void whenSendNotificationReturnSuccess_ShouldTryOneTime() {
        SendNotificationResponse response = mock(SendNotificationResponse.class);
        when(response.getStatus()).thenReturn("success");
        when(apiGateway.sendNotification()).thenReturn(response);

        transferenceService.sendNotification();

        verify(apiGateway, times(1)).sendNotification();
    }

    @Test
    void whenSendNotificationReturnFailOneTime_ShouldRetryOneTime() {
        SendNotificationResponse failResponse = mock(SendNotificationResponse.class);
        when(failResponse.getStatus()).thenReturn("fail");

        SendNotificationResponse successResponse = mock(SendNotificationResponse.class);
        when(successResponse.getStatus()).thenReturn("success");

        when(apiGateway.sendNotification())
                .thenReturn(failResponse)
                .thenReturn(successResponse);

        transferenceService.sendNotification();

        verify(apiGateway, times(2)).sendNotification();
    }

    @Test
    void whenSendNotificationReturnFailMultipleTimes_ShouldRetryMultipleTimes() {
        SendNotificationResponse failResponse = mock(SendNotificationResponse.class);
        when(failResponse.getStatus()).thenReturn("fail");

        SendNotificationResponse successResponse = mock(SendNotificationResponse.class);
        when(successResponse.getStatus()).thenReturn("success");

        when(apiGateway.sendNotification())
                .thenReturn(failResponse)
                .thenReturn(failResponse)
                .thenReturn(failResponse)
                .thenReturn(successResponse);

        transferenceService.sendNotification();

        verify(apiGateway, times(4)).sendNotification();
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
