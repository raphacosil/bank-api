package com.example.bank_api.integration;

import com.example.bank_api.exception.NotFoundException;
import com.example.bank_api.exception.UnprocessableEntityException;
import com.example.bank_api.gateway.ApiGateway;
import com.example.bank_api.gateway.dto.AuthorizeResponse;
import com.example.bank_api.gateway.dto.AuthorizeResponseData;
import com.example.bank_api.gateway.dto.SendNotificationResponse;
import com.example.bank_api.model.Balance;
import com.example.bank_api.model.Customer;
import com.example.bank_api.model.Transference;
import com.example.bank_api.repository.BalanceRepository;
import com.example.bank_api.repository.CustomerRepository;
import com.example.bank_api.repository.TransferenceRepository;
import com.example.bank_api.service.TransferenceService;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@SpringBootTest
@Testcontainers
@ActiveProfiles("test")
@Transactional
public class TransferenceServiceTest {
    @Autowired
    TransferenceRepository transferenceRepository;
    @Autowired
    BalanceRepository balanceRepository;
    @Autowired
    CustomerRepository customerRepository;

    @MockitoBean
    ApiGateway apiGateway;
    @Autowired
    TransferenceService transferenceService;

    Customer payer;
    Customer payee;

    @BeforeEach
    void setup() {
        payer = customerRepository.save(
                new Customer(null, "Payer", "12345678900", "payer@gmail.com", "password", false)
        );
        payee = customerRepository.save(
                new Customer(null, "Payee", "09876543211", "payee@gmail.com", "password", false)
        );

        balanceRepository.save(new Balance(null, payer.getId(), 100.00));
        balanceRepository.save(new Balance(null, payee.getId(), 100.00));
    }

    @AfterEach
    void close() {
        transferenceRepository.deleteAll();
        balanceRepository.deleteAll();
        customerRepository.deleteAll();
    }

    @Container
    static MySQLContainer<?> mysql = new MySQLContainer<>("mysql:8.0")
            .withDatabaseName("testdb")
            .withUsername("test")
            .withPassword("test");

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", mysql::getJdbcUrl);
        registry.add("spring.datasource.username", mysql::getUsername);
        registry.add("spring.datasource.password", mysql::getPassword);
        registry.add("spring.datasource.driver-class-name", () -> "com.mysql.cj.jdbc.Driver");
    }

    @Test
    void whenTransferAndNotEnoughBalance_thenThrowUnprocessableEntityException() {
        Transference transference = new Transference(null, payer.getId(), payee.getId(), 150.00, null);

        UnprocessableEntityException exception = assertThrows(UnprocessableEntityException.class, () -> transferenceService.transfer(transference));

        Optional<Double> payerBalance = balanceRepository.findAmountByCustomerId(payer.getId());
        Optional<Double> payeeBalance = balanceRepository.findAmountByCustomerId(payee.getId());

        Assertions.assertTrue(payerBalance.isPresent());
        Assertions.assertTrue(payeeBalance.isPresent());
        assertEquals(Double.valueOf(100.00), payerBalance.get());
        assertEquals(Double.valueOf(100.00), payeeBalance.get());

        assertEquals("Unprocessable entity Not enough balance", exception.getMessage());
    }

    @Test
    void whenTransferAndAuthorizeFalse_thenRefund() {
        when(apiGateway.authorize()).thenReturn(
                new AuthorizeResponse("fail", new AuthorizeResponseData(false))
        );
        when(apiGateway.sendNotification()).thenReturn(
                new SendNotificationResponse("success", null)
        );

        Transference transference = new Transference(null, payer.getId(), payee.getId(), 100.00, null);

        transferenceService.transfer(transference);

        Optional<Double> payerBalance = balanceRepository.findAmountByCustomerId(payer.getId());
        Optional<Double> payeeBalance = balanceRepository.findAmountByCustomerId(payee.getId());

        Assertions.assertTrue(payerBalance.isPresent());
        Assertions.assertTrue(payeeBalance.isPresent());
        assertEquals(Double.valueOf(100.00), payerBalance.get());
        assertEquals(Double.valueOf(100.00), payeeBalance.get());
    }

    @Test
    void whenTransfer_thenReturnVoid() {
        when(apiGateway.authorize()).thenReturn(
                new AuthorizeResponse("success", new AuthorizeResponseData(true))
        );
        when(apiGateway.sendNotification()).thenReturn(
                new SendNotificationResponse("success", null)
        );

        Transference transference = new Transference(null, payer.getId(), payee.getId(), 100.00, null);

        transferenceService.transfer(transference);

        Optional<Double> payerBalance = balanceRepository.findAmountByCustomerId(payer.getId());
        Optional<Double> payeeBalance = balanceRepository.findAmountByCustomerId(payee.getId());

        Assertions.assertTrue(payerBalance.isPresent());
        Assertions.assertTrue(payeeBalance.isPresent());
        assertEquals(Double.valueOf(0), payerBalance.get());
        assertEquals(Double.valueOf(200.00), payeeBalance.get());
    }

    @Test
    void whenRefundAndTransferenceEmpty_thenThrowNotFoundException() {
        assertThrows(NotFoundException.class, () -> transferenceService.refund(2L));
    }

    @Test
    void whenRefundAndNotEnoughBalance_thenThrowUnprocessableEntityException() {
        Transference transference = new Transference(null, 1L, 2L, 150.00, null);

        transferenceRepository.save(transference);

        UnprocessableEntityException exception = assertThrows(UnprocessableEntityException.class, () -> transferenceService.refund(1L));

        assertEquals("Unprocessable entity Not enough balance", exception.getMessage());
    }

    @Test
    void whenRefund_thenReturnVoid() {
        when(apiGateway.authorize()).thenReturn(
                new AuthorizeResponse("success", new AuthorizeResponseData(true))
        );
        when(apiGateway.sendNotification()).thenReturn(
                new SendNotificationResponse("success", null)
        );

        Transference transference = new Transference(null, payer.getId(), payee.getId(), 100.00, null);

        transferenceService.transfer(transference);

        Long id = transferenceRepository.findAll().getFirst().getId();

        transferenceService.refund(id);

        Optional<Double> payerBalance = balanceRepository.findAmountByCustomerId(payer.getId());
        Optional<Double> payeeBalance = balanceRepository.findAmountByCustomerId(payee.getId());

        Assertions.assertTrue(payerBalance.isPresent());
        Assertions.assertTrue(payeeBalance.isPresent());
        assertEquals(Double.valueOf(100.00), payerBalance.get());
        assertEquals(Double.valueOf(100.00), payeeBalance.get());
    }
}
