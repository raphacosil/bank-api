package com.example.bank_api.domain.service.customer;

import com.example.bank_api.config.exception.NotFoundException;
import com.example.bank_api.domain.model.Customer;
import com.example.bank_api.infra.repository.CustomerRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@AllArgsConstructor
public class UpdateCustomerUseCase {

    private final CustomerRepository customerRepository;

    public void execute(Long customerId, Customer newCustomer){
        Optional<Customer> oldCustomer = customerRepository.findById(customerId);
        if(oldCustomer.isEmpty()){
            throw new NotFoundException();
        }
        BeanUtils.copyProperties(newCustomer, oldCustomer.get(), "id", "createdAt");

        customerRepository.save(oldCustomer.get());
    }
}
