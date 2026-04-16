package com.example.bank_api.domain.service.customer;

import com.example.bank_api.domain.model.Balance;
import com.example.bank_api.domain.model.Customer;
import com.example.bank_api.infra.repository.BalanceRepository;
import com.example.bank_api.infra.repository.CustomerRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class SaveCustomerUseCase {

    private final CustomerRepository customerRepository;
    private final BalanceRepository balanceRepository;

    public void execute(Customer customer) {
        Customer customer1 =  customerRepository.save(customer);
        Balance balance = new Balance();
        balance.setCustomerId(customer1.getId());
        balance.setAmount(0.0);
        balanceRepository.save(balance);
    }
}
