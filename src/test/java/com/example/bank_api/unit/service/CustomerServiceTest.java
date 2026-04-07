package com.example.bank_api.unit.service;

import com.example.bank_api.exception.NotFoundException;
import com.example.bank_api.model.Balance;
import com.example.bank_api.model.Customer;
import com.example.bank_api.repository.BalanceRepository;
import com.example.bank_api.repository.CustomerRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CustomerServiceTest {

    @Mock
    CustomerRepository customerRepository;

    @Mock
    BalanceRepository balanceRepository;

    @InjectMocks
    CustomerService customerService;

    @Test
    void whenSave_ShouldSaveCustomerAndBalance_thenReturnVoid(){
        Customer customer = new Customer(1L, "Name", "documentNumber", "email", "password", false);

        when(balanceRepository.save(any(Balance.class))).thenReturn(new Balance());
        when(customerRepository.save(customer)).thenReturn(customer);

        customerService.save(customer);

        verify(customerRepository, times(1)).save(customer);
        verify(balanceRepository, times(1)).save(any(Balance.class));
    }

    @Test
    void whenFindAll_thenReturnCustomerList() {
        Customer customer = new Customer(1L, "Name", "documentNumber", "email", "password", false);
        List<Customer> customerList = List.of(customer);
        when(customerRepository.findAll()).thenReturn(customerList);

        customerService.findAll();

        verify(customerRepository, times(1)).findAll();
    }

    @Test
    void whenFindById_thenReturnCustomer() {
        Customer customer = new Customer(1L, "Name", "documentNumber", "email", "password", false);
        when(customerRepository.findById(1L)).thenReturn(Optional.of(customer));

        customerService.findById(1L);

        verify(customerRepository, times(1)).findById(1L);
    }

    @Test
    void whenFindById_thenThrowNotFoundException(){
        when(customerRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> customerService.findById(1L));

        verify(customerRepository, times(1)).findById(1L);
    }

    @Test
    void whenUpdate_ShouldFindByIdAndSave_thenReturnVoid(){
        Customer customer = new Customer(1L, "Name", "documentNumber", "email", "password", false);
        Optional<Customer> customerOptional = Optional.of(customer);
        when(customerRepository.findById(1L)).thenReturn(customerOptional);
        when(customerRepository.save(customer)).thenReturn(customer);

        customerService.update(1L, customer);

        verify(customerRepository, times(1)).findById(1L);
        verify(customerRepository, times(1)).save(customer);
    }

    @Test
    void whenUpdate_ShouldFindByIdAndSave_thenThrowNotFoundException(){
        Customer customer = new Customer(1L, "Name", "documentNumber", "email", "password", false);
        Optional<Customer> optional = Optional.empty();
        when(customerRepository.findById(1L)).thenReturn(optional);

        assertThrows(NotFoundException.class, () -> customerService.update(1L, customer));

        verify(customerRepository, times(1)).findById(1L);
        verify(customerRepository, times(0)).save(customer);
    }

    @Test
    void whenDelete_ShouldFindByIdAndDelete_thenReturnVoid(){
        Customer customer = new Customer(1L, "Name", "documentNumber", "email", "password", false);
        Optional<Customer> customerOptional = Optional.of(customer);
        when(customerRepository.findById(1L)).thenReturn(customerOptional);
        doNothing().when(customerRepository).delete(customer);

        customerService.delete(1L);

        verify(customerRepository, times(1)).findById(1L);
        verify(customerRepository, times(1)).delete(customer);
    }

    @Test
    void whenUpdate_ShouldFindByIdAndDelete_thenThrowNotFoundException(){
        Customer customer = new Customer(1L, "Name", "documentNumber", "email", "password", false);
        Optional<Customer> optional = Optional.empty();
        when(customerRepository.findById(1L)).thenReturn(optional);

        assertThrows(NotFoundException.class, () -> customerService.delete(1L));

        verify(customerRepository, times(1)).findById(1L);
        verify(customerRepository, times(0)).delete(customer);
    }
}
