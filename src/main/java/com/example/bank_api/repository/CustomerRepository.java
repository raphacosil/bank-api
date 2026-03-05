package com.example.bank_api.repository;

import com.example.bank_api.model.Balance;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerRepository extends JpaRepository<Balance, Long> {

}
