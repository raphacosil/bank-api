package com.example.bank_api.domain.service.balance;

import com.example.bank_api.config.exception.NotFoundException;
import com.example.bank_api.infra.repository.BalanceRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@AllArgsConstructor
public class FindBalanceAmountByCustomer {

    private final BalanceRepository balanceRepository;

    public Double execute(Long customerId){
        Optional<Double> amount = balanceRepository.findAmountByCustomerId(customerId);
        if(amount.isEmpty()){
            throw new NotFoundException();
        }
        return amount.get();
    }
}
