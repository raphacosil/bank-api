package com.example.bank_api.unit.controller;

import com.example.bank_api.controller.CustomerController;
import com.example.bank_api.exception.BadRequestException;
import com.example.bank_api.model.Customer;
import com.example.bank_api.model.dto.GetCustomerDto;
import com.example.bank_api.service.CustomerService;
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

import static org.junit.Assert.assertThrows;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class CustomerControllerTest {

    @Mock
    CustomerService customerService;

    @InjectMocks
    CustomerController customerController;

    Customer customer;

    @Mock
    BindingResult bindingResult;

    @Mock
    private FieldError fieldError;

    @BeforeEach
    void setUp() {
        customer = new Customer(
                1L,
                "name",
                "documentNumber",
                "email@email.com",
                "password",
                false
        );
    }

    @Test
    void whenSave_ShouldReturn201() {
        doNothing().when(customerService).save(customer);

        ResponseEntity<Void> response = customerController.save(customer, bindingResult);

        assertEquals(HttpStatusCode.valueOf(201), response.getStatusCode());
        verify(customerService, times(1)).save (customer);
    }

    @Test
    void whenSave_ShouldReturn400() {
        when(bindingResult.hasErrors()).thenReturn(true);
        when(bindingResult.getFieldError()).thenReturn(fieldError);
        when(fieldError.getDefaultMessage()).thenReturn("Nome é obrigatório");

        BadRequestException exception = assertThrows(BadRequestException.class, () ->  customerController.save(customer, bindingResult));

        assertEquals("Bad request Nome é obrigatório", exception.getMessage());
        verify(customerService, times(0)).save (customer);
    }

    @Test
    void whenUpdate_ShouldReturn200() {
        doNothing().when(customerService).update(1L, customer);

        ResponseEntity<Void> response = customerController.update(1L, customer, bindingResult);

        assertEquals(HttpStatusCode.valueOf(200), response.getStatusCode());
        verify(customerService, times(1)).update (1L, customer);
    }

    @Test
    void whenUpdate_ShouldReturn400() {
        when(bindingResult.hasErrors()).thenReturn(true);
        when(bindingResult.getFieldError()).thenReturn(fieldError);
        when(fieldError.getDefaultMessage()).thenReturn("Nome é obrigatório");

        BadRequestException exception = assertThrows(BadRequestException.class, () ->  customerController.update(1L, customer, bindingResult));

        assertEquals("Bad request Nome é obrigatório", exception.getMessage());
        verify(customerService, times(0)).update(1L, customer);
    }

    @Test
    void whenDelete_ShouldReturn200() {
        doNothing().when(customerService).delete(1L);

        ResponseEntity<Void> response = customerController.delete(1L);

        assertEquals(HttpStatusCode.valueOf(200), response.getStatusCode());
        verify(customerService, times(1)).delete(1L);
    }

    @Test
    void whenFindById_shouldReturn200() {
        GetCustomerDto getCustomerDto = new GetCustomerDto(
                1L,
                "name",
                "documentNumber",
                "email@email.com",
                false
        );
        when(customerService.findById(1L)).thenReturn(getCustomerDto);

        ResponseEntity<GetCustomerDto> response = customerController.findById(1L);

        assertEquals(HttpStatusCode.valueOf(200), response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1L, response.getBody().getId());
        assertEquals("name", response.getBody().getName());
        assertEquals("documentNumber", response.getBody().getDocumentNumber());
        assertEquals("email@email.com", response.getBody().getEmail());
        assertFalse(response.getBody().isBusiness());

        verify(customerService, times(1)).findById(1L);
    }

    @Test
    void whenFindAll_shouldReturn200() {
        GetCustomerDto getCustomerDto = new GetCustomerDto(
                1L,
                "name",
                "documentNumber",
                "email@email.com",
                false
        );

        List<GetCustomerDto> list = List.of(getCustomerDto);
        when(customerService.findAll()).thenReturn(list);

        ResponseEntity<List<GetCustomerDto>> response = customerController.findAll();

        assertEquals(HttpStatusCode.valueOf(200), response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1L, response.getBody().getFirst().getId());
        assertEquals("name", response.getBody().getFirst().getName());
        assertEquals("documentNumber", response.getBody().getFirst().getDocumentNumber());
        assertEquals("email@email.com", response.getBody().getFirst().getEmail());
        assertFalse(response.getBody().getFirst().isBusiness());

        verify(customerService, times(1)).findAll();
    }
}
