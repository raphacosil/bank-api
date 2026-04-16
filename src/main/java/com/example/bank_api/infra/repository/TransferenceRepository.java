package com.example.bank_api.infra.repository;

import com.example.bank_api.domain.model.Transference;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TransferenceRepository extends JpaRepository<Transference, Long> {

    @Query("""
            SELECT t FROM Transference t
            WHERE t.payer = :customerId OR t.payee = :customerId
            ORDER BY t.createdAt DESC
            """)
    List<Transference> findByCustomer(@Param("customerId") Long customerId);

    @Query("""
            SELECT t FROM Transference t
            WHERE (t.payer = :firstCustomerId AND t.payee = :secondCustomerId)
               OR (t.payer = :secondCustomerId AND t.payee = :firstCustomerId)
            ORDER BY t.createdAt DESC
            """)
    List<Transference> findByCustomer(
            @Param("firstCustomerId") Long firstCustomerId,
            @Param("secondCustomerId") Long secondCustomerId
    );

    @Query("""
            SELECT COALESCE(SUM(CASE WHEN t.payee = :customerId THEN t.value ELSE 0 END), 0) -
                   COALESCE(SUM(CASE WHEN t.payer = :customerId THEN t.value ELSE 0 END), 0)
            FROM Transference t
            WHERE t.payer = :customerId OR t.payee = :customerId
            """)
    Double getBalance(@Param("customerId") Long customerId);
}



