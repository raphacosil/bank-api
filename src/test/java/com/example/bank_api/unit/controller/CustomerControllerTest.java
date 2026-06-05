package com.example.bank_api.unit.controller;

import com.example.bank_api.boundary.controller.CustomerController;
import com.example.bank_api.config.exception.BadRequestException;
import com.example.bank_api.domain.model.Customer;
import com.example.bank_api.domain.use_case.customer.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import java.util.List;
import java.util.Objects;

import static org.junit.Assert.assertThrows;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class CustomerControllerTest {

    @Mock
    SaveCustomerUseCase saveCustomerUseCase;

    @Mock
    UpdateCustomerUseCase updateCustomerUseCase;

    @Mock
    DeleteCustomerUseCase deleteCustomerUseCase;

    @Mock
    FindAllCustomersUseCase findAllCustomersUseCase;

    @Mock
    FindCustomerByIdUseCase findCustomerByIdUseCase;

    @InjectMocks
    CustomerController customerController;

    Customer customer;

    @Mock
    BindingResult bindingResult;

    @Mock
    private FieldError fieldError;

    @BeforeEach
    void setUp() {
        customer = Customer.builder()
                .id(1L)
                .name("name")
                .isBusiness(false)
                .build();
    }

    @Test
    void whenSave_ShouldReturn201() {
        doNothing().when(saveCustomerUseCase).execute(customer);

        ResponseEntity<Void> response = customerController.save(customer, bindingResult);

        assertEquals(HttpStatusCode.valueOf(201), response.getStatusCode());
        verify(saveCustomerUseCase, times(1)).execute(customer);
    }

    @Test
    void whenSave_ShouldReturn400() {
        when(bindingResult.hasErrors()).thenReturn(true);
        when(Objects.requireNonNull(bindingResult.getFieldError())).thenReturn(fieldError);
        when(Objects.requireNonNull(fieldError.getDefaultMessage())).thenReturn("Nome é obrigatório");

        BadRequestException exception = assertThrows(BadRequestException.class, () ->  customerController.save(customer, bindingResult));

        assertEquals("Bad request Nome é obrigatório", exception.getMessage());
        verify(saveCustomerUseCase, times(0)).execute(customer);
    }

    @Test
    void whenUpdate_ShouldReturn200() {
        doNothing().when(updateCustomerUseCase).execute(1L, customer);

        ResponseEntity<Void> response = customerController.update(1L, customer, bindingResult);

        assertEquals(HttpStatusCode.valueOf(200), response.getStatusCode());
        verify(updateCustomerUseCase, times(1)).execute(1L, customer);
    }

    @Test
    void whenUpdate_ShouldReturn400() {
        when(bindingResult.hasErrors()).thenReturn(true);
        when(Objects.requireNonNull(bindingResult.getFieldError())).thenReturn(fieldError);
        when(Objects.requireNonNull(fieldError.getDefaultMessage())).thenReturn("Nome é obrigatório");

        BadRequestException exception = assertThrows(BadRequestException.class, () ->  customerController.update(1L, customer, bindingResult));

        assertEquals("Bad request Nome é obrigatório", exception.getMessage());
        verify(updateCustomerUseCase, times(0)).execute(1L, customer);
    }

    @Test
    void whenDelete_ShouldReturn200() {
        doNothing().when(deleteCustomerUseCase).execute(1L);

        ResponseEntity<Void> response = customerController.delete(1L);

        assertEquals(HttpStatusCode.valueOf(200), response.getStatusCode());
        verify(deleteCustomerUseCase, times(1)).execute(1L);
    }

    @Test
    void whenFindById_shouldReturn200() {

        when(findCustomerByIdUseCase.execute(1L)).thenReturn(customer);

        ResponseEntity<Customer> response = customerController.findById(1L);

        assertEquals(HttpStatusCode.valueOf(200), response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1L, response.getBody().getId());
        assertEquals("name", response.getBody().getName());
        assertFalse(response.getBody().isBusiness());

        verify(findCustomerByIdUseCase, times(1)).execute(1L);
    }

    @Test
    void whenFindAll_shouldReturn200() {
        List<Customer> list = List.of(customer);
        when(findAllCustomersUseCase.execute()).thenReturn(list);

        ResponseEntity<List<Customer>> response = customerController.findAll();

        assertEquals(HttpStatusCode.valueOf(200), response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1L, response.getBody().getFirst().getId());
        assertEquals("name", response.getBody().getFirst().getName());
        assertFalse(response.getBody().getFirst().isBusiness());

        verify(findAllCustomersUseCase, times(1)).execute();
    }
}
