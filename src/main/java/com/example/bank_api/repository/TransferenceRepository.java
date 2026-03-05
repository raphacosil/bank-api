package com.example.bank_api.repository;

import com.example.bank_api.model.Balance;
import com.example.bank_api.model.Transference;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TransferenceRepository extends JpaRepository<Transference, Long> {

    @Query("""
            SELECT * FROM transference AS t
            WHERE t.payer_id = :customerId OR t.receiver_id = :customerId
            ORDER BY t.created_at DESC
            """)
    List<Transference> findByCustomer(@Param("customerId") Long customerId);

    @Query("""
            SELECT * FROM transference AS t WHERE t.payer_id = :firstCustomerId AND t.receiver_id = :secondCustomerId
            UNION
            SELECT * FROM transference AS t WHERE t.payer_id = :secondCustomerId AND t.receiver_id = :firstCustomerId
            ORDER BY created_at DESC
            """)
    List<Transference> findByCustomer(
            @Param("firstCustomerId") Long firstCustomerId,
            @Param("secondCustomerId") Long secondCustomerId
    );

    @Query("""
            (SELECT COALESCE(SUM(t.amount), 0) FROM transference WHERE t.receiver_id = :customerId) -
            (SELECT COALESCE(SUM(t.amount), 0) FROM transference WHERE t.payer_id = :customerId)
            """)
    Double getBalance(@Param("customerId") Long customerId);
}
