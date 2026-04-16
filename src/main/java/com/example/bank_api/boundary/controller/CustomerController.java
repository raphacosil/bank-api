package com.example.bank_api.boundary.controller;

import com.example.bank_api.boundary.contract.CustomerContract;
import com.example.bank_api.config.exception.BadRequestException;
import com.example.bank_api.domain.model.Customer;
import com.example.bank_api.domain.service.customer.*;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Objects;

@Tag(name = "Customer", description = "Endpoints for managing customers")
@RestController
@RequestMapping("/customer")
@AllArgsConstructor
public class CustomerController implements CustomerContract {

    private final SaveCustomerUseCase saveCustomerUseCase;
    private final UpdateCustomerUseCase updateCustomerUseCase;
    private final DeleteCustomerUseCase deleteCustomerUseCase;
    private final FindAllCustomersUseCase findAllCustomersUseCase;
    private final FindCustomerByIdUseCase findCustomerByIdUseCase;

    @Override
    public ResponseEntity<Void> save(Customer customer, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new BadRequestException(
                    Objects.requireNonNull(bindingResult.getFieldError()).getDefaultMessage()
            );
        }

        saveCustomerUseCase.execute(customer);
        return ResponseEntity.status(201).build();
    }

    @Override
    public ResponseEntity<Void> update(Long id, Customer customer, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new BadRequestException(
                    Objects.requireNonNull(bindingResult.getFieldError()).getDefaultMessage()
            );
        }
        updateCustomerUseCase.execute(id, customer);

        return ResponseEntity.status(200).build();
    }

    @Override
    public ResponseEntity<Void> delete(Long customerId) {
        deleteCustomerUseCase.execute(customerId);

        return ResponseEntity.status(200).build();
    }

    @Override
    public ResponseEntity<List<Customer>> findAll() {
        return ResponseEntity.ok(findAllCustomersUseCase.execute());
    }

    @Override
    public ResponseEntity<Customer> findById(Long customerId) {
        return ResponseEntity.ok(findCustomerByIdUseCase.execute(customerId));
    }
}
