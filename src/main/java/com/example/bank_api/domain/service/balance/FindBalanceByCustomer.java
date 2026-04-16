package com.example.bank_api.domain.service.balance;

import com.example.bank_api.config.exception.NotFoundException;
import com.example.bank_api.domain.model.Balance;
import com.example.bank_api.infra.repository.BalanceRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@AllArgsConstructor
public class FindBalanceByCustomer {

    private final BalanceRepository balanceRepository;

    public Balance execute(Long customerId){
        Optional<Balance> balance = balanceRepository.findByCustomerId(customerId);
        if(balance.isEmpty()){
            throw new NotFoundException();
        }
        return balance.get();
    }
}
