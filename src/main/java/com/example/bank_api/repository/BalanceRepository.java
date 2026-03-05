package com.example.bank_api.repository;

import com.example.bank_api.model.Balance;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BalanceRepository extends JpaRepository<Balance, Long> {
     Optional<Balance> findByCustomerId(Long customerId);

     Optional<Double> findAmountByCustomerId(Long customerId);
}
