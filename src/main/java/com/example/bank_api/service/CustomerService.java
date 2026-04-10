package com.example.bank_api.service;

import com.example.bank_api.exception.NotFoundException;
import com.example.bank_api.model.Balance;
import com.example.bank_api.model.Customer;
import com.example.bank_api.model.dto.GetCustomerDto;
import com.example.bank_api.repository.BalanceRepository;
import com.example.bank_api.repository.CustomerRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CustomerService {

    CustomerRepository customerRepository;

    BalanceRepository balanceRepository;

    public void save(Customer customer) {
        Customer customer1 =  customerRepository.save(customer);
        Balance balance = new Balance();
        balance.setCustomerId(customer1.getId());
        balance.setAmount(0.0);
        balanceRepository.save(balance);
    }

    public void update(Long customerId, Customer newCustomer){
        Optional<Customer> oldCustomer = customerRepository.findById(customerId);
        if(oldCustomer.isEmpty()){
            throw new NotFoundException();
        }
        BeanUtils.copyProperties(newCustomer, oldCustomer.get(), "id", "createdAt");

        customerRepository.save(oldCustomer.get());
    }

    public void delete(Long customerId){
        Optional<Customer> customer = customerRepository.findById(customerId);

        if(customer.isEmpty()){
            throw new NotFoundException();
        }

        customerRepository.delete(customer.get());
    }

    public List<GetCustomerDto> findAll(){
        return mapCustomerListToGetCustomerDtoList(customerRepository.findAll());
    }

    public GetCustomerDto findById(Long customerId){
        Optional<Customer> customer = customerRepository.findById(customerId);

        if(customer.isEmpty()){
            throw new NotFoundException();
        }

        return mapCustomerToGetCustomerDto(customer.get());
    }

    private GetCustomerDto mapCustomerToGetCustomerDto(Customer customer){
        return new GetCustomerDto(
                customer.getId(),
                customer.getName(),
                customer.getEmail(),
                customer.getDocumentNumber(),
                customer.isBusiness()
        );
    }

    private List<GetCustomerDto> mapCustomerListToGetCustomerDtoList(List<Customer> customerList){
        return customerList.stream().map(this::mapCustomerToGetCustomerDto).toList();
    }
}
