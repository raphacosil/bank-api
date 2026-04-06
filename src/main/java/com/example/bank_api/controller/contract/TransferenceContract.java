package com.example.bank_api.controller.contract;

import com.example.bank_api.model.Transference;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

public interface TransferenceContract {
    @PostMapping("/")
    @Operation(summary = "Make a transference")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Transference made successfully"),
            @ApiResponse(responseCode = "400", description = "Bad request"),
            @ApiResponse(responseCode = "422", description = "Unprocessable entity"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    ResponseEntity<Void> transfer(
            @Valid @RequestBody Transference transference,
            BindingResult bindingResult
    );

    @DeleteMapping("/{id}/refund")
    @Operation(summary = "Refund a transference")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Transference refunded successfully"),
            @ApiResponse(responseCode = "404", description = "Transference not found"),
            @ApiResponse(responseCode = "422", description = "Unprocessable entity"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    ResponseEntity<Void> refund(@PathVariable(value = "id") Long transferenceId);

    @GetMapping("/")
    @Operation(summary = "Get all transferences")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Transferences retrieved successfully"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    ResponseEntity<List<Transference>> findAll();

    @GetMapping("/{id}")
    @Operation(summary = "Get transference by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Transference retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Transference not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    ResponseEntity<Transference> findById(@PathVariable(value = "id") Long id);

    @GetMapping("/customer/{customerId}")
    @Operation(summary = "Get transferences by customer id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Transferences retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Transferences not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    ResponseEntity<List<Transference>> findByCustomer(@PathVariable(value = "customerId") Long customerId);

    @GetMapping("/between/{firstCustomerId}/{secondCustomerId}")
    @Operation(summary = "Get transferences between two customers")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Transferences retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Transferences not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    ResponseEntity<List<Transference>> findBetweenCustomers(
            @PathVariable(value = "firstCustomerId") Long firstCustomerId,
            @PathVariable(value = "secondCustomerId") Long secondCustomerId
    );
}
