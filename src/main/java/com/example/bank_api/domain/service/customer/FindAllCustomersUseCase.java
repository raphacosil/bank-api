package com.example.bank_api.domain.service.customer;

import com.example.bank_api.domain.model.Customer;
import com.example.bank_api.infra.repository.CustomerRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@AllArgsConstructor
public class FindAllCustomersUseCase {

    private final CustomerRepository customerRepository;

    public List<Customer> execute(){
        return customerRepository.findAll();
    }
}
