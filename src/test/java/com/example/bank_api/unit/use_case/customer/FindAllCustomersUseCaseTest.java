package com.example.bank_api.unit.use_case.customer;

import com.example.bank_api.domain.model.Customer;
import com.example.bank_api.domain.use_case.customer.FindAllCustomersUseCase;
import com.example.bank_api.infra.repository.CustomerRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class FindAllCustomersUseCaseTest {

    @Mock
    CustomerRepository customerRepository;

    @InjectMocks
    FindAllCustomersUseCase findAllCustomersUseCase;

    @Test
    void whenExecute_thenReturnCustomerList() {
        Customer customer = Customer.builder()
                .id(1L)
                .name("name")
                .isBusiness(false)
                .build();
        List<Customer> customerList = List.of(customer);
        when(customerRepository.findAll()).thenReturn(customerList);

        findAllCustomersUseCase.execute();

        verify(customerRepository, times(1)).findAll();
    }
}
