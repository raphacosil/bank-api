package com.example.bank_api.repository;

import com.example.bank_api.model.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface CustomerRepository extends JpaRepository<Customer, Long> {
    @Query("SELECT c.isBusiness FROM Customer c WHERE c.id = :id")
    Boolean findIsBusinessById(Long id);
}
