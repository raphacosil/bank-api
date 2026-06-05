package com.example.bank_api.integration;

import com.example.bank_api.config.exception.NotFoundException;
import com.example.bank_api.config.exception.UnprocessableEntityException;
import com.example.bank_api.infra.gateway.ApiGateway;
import com.example.bank_api.infra.gateway.dto.AuthorizeResponse;
import com.example.bank_api.infra.gateway.dto.AuthorizeResponseData;
import com.example.bank_api.infra.gateway.dto.SendNotificationResponse;
import com.example.bank_api.domain.model.Balance;
import com.example.bank_api.domain.model.Customer;
import com.example.bank_api.domain.model.Transference;
import com.example.bank_api.infra.repository.BalanceRepository;
import com.example.bank_api.infra.repository.CustomerRepository;
import com.example.bank_api.infra.repository.TransferenceRepository;
import com.example.bank_api.domain.use_case.transference.RefundUseCase;
import com.example.bank_api.domain.use_case.transference.TransferUseCase;
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
public class TransferenceIntegrationTest {
    @Autowired
    TransferenceRepository transferenceRepository;
    @Autowired
    BalanceRepository balanceRepository;
    @Autowired
    CustomerRepository customerRepository;

    @MockitoBean
    ApiGateway apiGateway;

    @Autowired
    TransferUseCase transferUseCase;

    @Autowired
    RefundUseCase refundUseCase;

    Customer payer;
    Customer payee;

    @BeforeEach
    void setup() {
        payer = Customer.builder()
                .id(null)
                .name("name")
                .isBusiness(false)
                .documentNumber("12345678900")
                .email("payer@gmail.com")
                .build();
        customerRepository.save(payer);

        payee = Customer.builder()
                .id(null)
                .name("name")
                .isBusiness(false)
                .documentNumber("09876543211")
                .email("payee@gmail.com")
                .build();
        customerRepository.save(payee);

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

        UnprocessableEntityException exception = assertThrows(UnprocessableEntityException.class, () -> transferUseCase.execute(transference));

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

        transferUseCase.execute(transference);

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

        transferUseCase.execute(transference);

        Optional<Double> payerBalance = balanceRepository.findAmountByCustomerId(payer.getId());
        Optional<Double> payeeBalance = balanceRepository.findAmountByCustomerId(payee.getId());

        Assertions.assertTrue(payerBalance.isPresent());
        Assertions.assertTrue(payeeBalance.isPresent());
        assertEquals(Double.valueOf(0), payerBalance.get());
        assertEquals(Double.valueOf(200.00), payeeBalance.get());
    }

    @Test
    void whenRefundAndTransferenceEmpty_thenThrowNotFoundException() {
        assertThrows(NotFoundException.class, () -> refundUseCase.execute(2L));
    }

    @Test
    void whenRefundAndNotEnoughBalance_thenThrowUnprocessableEntityException() {
        Transference transference = new Transference(null, 1L, 2L, 150.00, null);

        transferenceRepository.save(transference);

        UnprocessableEntityException exception = assertThrows(UnprocessableEntityException.class, () -> refundUseCase.execute(1L));

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

        transferUseCase.execute(transference);

        Long id = transferenceRepository.findAll().getFirst().getId();

        refundUseCase.execute(id);

        Optional<Double> payerBalance = balanceRepository.findAmountByCustomerId(payer.getId());
        Optional<Double> payeeBalance = balanceRepository.findAmountByCustomerId(payee.getId());

        Assertions.assertTrue(payerBalance.isPresent());
        Assertions.assertTrue(payeeBalance.isPresent());
        assertEquals(Double.valueOf(100.00), payerBalance.get());
        assertEquals(Double.valueOf(100.00), payeeBalance.get());
    }
}
