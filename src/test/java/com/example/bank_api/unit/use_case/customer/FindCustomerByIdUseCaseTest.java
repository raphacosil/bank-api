package com.example.bank_api.unit.use_case.customer;

import com.example.bank_api.config.exception.NotFoundException;
import com.example.bank_api.domain.model.Customer;
import com.example.bank_api.domain.use_case.customer.FindCustomerByIdUseCase;
import com.example.bank_api.infra.repository.CustomerRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class FindCustomerByIdUseCaseTest {

    @Mock
    CustomerRepository customerRepository;

    @InjectMocks
    FindCustomerByIdUseCase findCustomerByIdUseCase;

    @Test
    void whenExecute_thenReturnCustomer() {
        Customer customer = Customer.builder()
                .id(1L)
                .name("name")
                .isBusiness(false)
                .build();
        when(customerRepository.findById(1L)).thenReturn(Optional.of(customer));

        findCustomerByIdUseCase.execute(1L);

        verify(customerRepository, times(1)).findById(1L);
    }

    @Test
    void whenExecute_thenThrowNotFoundException() {
        when(customerRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> findCustomerByIdUseCase.execute(1L));

        verify(customerRepository, times(1)).findById(1L);
    }
}
