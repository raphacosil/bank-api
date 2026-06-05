package com.example.bank_api.unit.use_case.customer;

import com.example.bank_api.config.exception.NotFoundException;
import com.example.bank_api.domain.model.Customer;
import com.example.bank_api.domain.use_case.customer.UpdateCustomerUseCase;
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
public class UpdateCustomerUseCaseTest {

    @Mock
    CustomerRepository customerRepository;

    @InjectMocks
    UpdateCustomerUseCase updateCustomerUseCase;

    @Test
    void whenExecute_ShouldFindByIdAndSave_thenReturnVoid() {
        Customer customer = Customer.builder()
                .id(1L)
                .name("name")
                .isBusiness(false)
                .build();

        Optional<Customer> customerOptional = Optional.of(customer);
        when(customerRepository.findById(1L)).thenReturn(customerOptional);
        when(customerRepository.save(customer)).thenReturn(customer);

        updateCustomerUseCase.execute(1L, customer);

        verify(customerRepository, times(1)).findById(1L);
        verify(customerRepository, times(1)).save(customer);
    }

    @Test
    void whenExecute_ShouldFindByIdAndSave_thenThrowNotFoundException() {
        Customer customer = Customer.builder()
                .id(1L)
                .name("name")
                .isBusiness(false)
                .build();

        Optional<Customer> optional = Optional.empty();
        when(customerRepository.findById(1L)).thenReturn(optional);

        assertThrows(NotFoundException.class, () -> updateCustomerUseCase.execute(1L, customer));

        verify(customerRepository, times(1)).findById(1L);
        verify(customerRepository, times(0)).save(customer);
    }
}
