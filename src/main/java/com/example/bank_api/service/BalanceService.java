package com.example.bank_api.service;

import com.example.bank_api.exception.NotFoundException;
import com.example.bank_api.model.Balance;
import com.example.bank_api.repository.BalanceRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class BalanceService {
    BalanceRepository balanceRepository;

    public Balance getBalanceByCustomer(Long customerId){
        Optional<Balance> balance = balanceRepository.findByCustomerId(customerId);
        if(balance.isEmpty()){
            throw new NotFoundException();
        }
        return balance.get();
    }

    public Double getBalanceAmountByCustomer(Long customerId){
        Optional<Double> amount = balanceRepository.findAmountByCustomerId(customerId);
        if(amount.isEmpty()){
            throw new NotFoundException();
        }
        return amount.get();
    }
}
