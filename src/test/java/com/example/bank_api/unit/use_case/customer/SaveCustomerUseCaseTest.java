package com.example.bank_api.unit.use_case.customer;

import com.example.bank_api.domain.model.Balance;
import com.example.bank_api.domain.model.Customer;
import com.example.bank_api.domain.use_case.customer.SaveCustomerUseCase;
import com.example.bank_api.infra.repository.BalanceRepository;
import com.example.bank_api.infra.repository.CustomerRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class SaveCustomerUseCaseTest {

    @Mock
    CustomerRepository customerRepository;

    @Mock
    BalanceRepository balanceRepository;

    @InjectMocks
    SaveCustomerUseCase saveCustomerUseCase;

    @Test
    void whenExecute_ShouldSaveCustomerAndBalance_thenReturnVoid() {
        Customer customer = Customer.builder()
                .id(1L)
                .name("name")
                .isBusiness(false)
                .build();

        when(customerRepository.save(customer)).thenReturn(customer);
        when(balanceRepository.save(any(Balance.class))).thenReturn(new Balance());

        saveCustomerUseCase.execute(customer);

        verify(customerRepository, times(1)).save(customer);
        verify(balanceRepository, times(1)).save(any(Balance.class));
    }
}
