package com.example.bank_api.unit.controller.contract;

import com.example.bank_api.model.Balance;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

public interface BalanceContract {
    @GetMapping("/{id}")
    @Operation(summary = "Get balance by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Balance retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Balance not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    ResponseEntity<Balance> findById(@PathVariable(value = "id") Long id);

    @GetMapping("/amount/{id}")
    @Operation(summary = "Get balance amount by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Balance amount retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Balance amount not found"),
            @ApiResponse(responseCode = "500", description = "Internal amount server error")
    })
    ResponseEntity<Double> findAmountById(@PathVariable(value = "id") Long id);
}
