package com.example.bank_api.domain.service.customer;

import com.example.bank_api.config.exception.NotFoundException;
import com.example.bank_api.domain.model.Customer;
import com.example.bank_api.infra.repository.CustomerRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@AllArgsConstructor
public class FindCustomerByIdUseCase {

    private final CustomerRepository customerRepository;

    public Customer execute(Long customerId){
        Optional<Customer> customer = customerRepository.findById(customerId);

        if(customer.isEmpty()){
            throw new NotFoundException();
        }

        return customer.get();
    }
}
