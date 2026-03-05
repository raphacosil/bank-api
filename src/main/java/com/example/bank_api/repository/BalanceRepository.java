package com.example.bank_api.repository;

import com.example.bank_api.model.Balance;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BalanceRepository extends JpaRepository<Balance, Long> {
     Balance findByCustomerId(String customerId);

     Double findAmountByCustomerId(String customerId);
}
