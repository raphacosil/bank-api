package com.example.bank_api.controller;

import com.example.bank_api.contract.BalanceContract;
import com.example.bank_api.model.Balance;
import com.example.bank_api.service.BalanceService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Balance", description = "Endpoints for managing balances")
@RestController
@RequestMapping("/balance")
public class BalanceController implements BalanceContract {
    BalanceService balanceService;
    @Override
    public ResponseEntity<Balance> findById(Long id) {
        return ResponseEntity.ok(balanceService.getBalanceByCustomer(id));
    }

    @Override
    public ResponseEntity<Double> findAmountById(Long id) {
        return ResponseEntity.ok(balanceService.getBalanceAmountByCustomer(id));
    }
}
