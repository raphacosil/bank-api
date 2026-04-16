package com.example.bank_api.boundary.controller;

import com.example.bank_api.boundary.contract.BalanceContract;
import com.example.bank_api.domain.model.Balance;
import com.example.bank_api.domain.service.balance.FindBalanceAmountByCustomer;
import com.example.bank_api.domain.service.balance.FindBalanceByCustomer;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Balance", description = "Endpoints for managing balances")
@RestController
@RequestMapping("/balance")
@AllArgsConstructor
public class BalanceController implements BalanceContract {

    private final FindBalanceByCustomer findBalanceByCustomer;
    private final FindBalanceAmountByCustomer findBalanceAmountByCustomer;

    @Override
    public ResponseEntity<Balance> findById(Long id) {
        return ResponseEntity.ok(findBalanceByCustomer.execute(id));
    }

    @Override
    public ResponseEntity<Double> findAmountById(Long id) {
        return ResponseEntity.ok(findBalanceAmountByCustomer.execute(id));
    }
}
