package com.example.bank_api.controller;

import com.example.bank_api.controller.contract.CustomerContract;
import com.example.bank_api.exception.BadRequestException;
import com.example.bank_api.model.Customer;
import com.example.bank_api.service.CustomerService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Objects;

@Tag(name = "Customer", description = "Endpoints for managing customers")
@RestController
@RequestMapping("/customer")
public class CustomerController implements CustomerContract {

    CustomerService customerService;

    @Override
    public ResponseEntity<Void> save(Customer customer, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new BadRequestException(
                    Objects.requireNonNull(bindingResult.getFieldError()).getDefaultMessage()
            );
        }

        customerService.save(customer);
        return ResponseEntity.status(201).build();
    }

    @Override
    public ResponseEntity<Void> update(Long id, Customer customer, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new BadRequestException(
                    Objects.requireNonNull(bindingResult.getFieldError()).getDefaultMessage()
            );
        }
        customerService.update(id, customer);

        return ResponseEntity.status(200).build();
    }

    @Override
    public ResponseEntity<Void> delete(Long customerId) {
        customerService.delete(customerId);

        return ResponseEntity.status(200).build();
    }

    @Override
    public ResponseEntity<List<Customer>> findAll() {
        return ResponseEntity.ok(customerService.findAll());
    }

    @Override
    public ResponseEntity<Customer> findById(Long customerId) {
        return ResponseEntity.ok(customerService.findById(customerId));
    }
}
