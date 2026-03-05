package com.example.bank_api.controller;

import com.example.bank_api.contract.TransferenceContract;
import com.example.bank_api.exception.BadRequestException;
import com.example.bank_api.model.Transference;
import com.example.bank_api.service.TransferenceService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Objects;

@Tag(name = "Transference", description = "Endpoints for managing transferences")
@RestController
@RequestMapping("/transference")
public class TransferenceController implements TransferenceContract {

    TransferenceService transferenceService;
    @Override
    public ResponseEntity<Void> transfer(Transference transference, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new BadRequestException(
                    Objects.requireNonNull(bindingResult.getFieldError()).getDefaultMessage()
            );
        }

        transferenceService.transfer(transference);

        return ResponseEntity.status(200).build();
    }

    @Override
    public ResponseEntity<Void> refund(Long transferenceId) {
        transferenceService.refund(transferenceId);

        return ResponseEntity.status(200).build();
    }

    @Override
    public ResponseEntity<Transference> findById(Long id) {
        return ResponseEntity.ok(transferenceService.findById(id));
    }

    @Override
    public ResponseEntity<List<Transference>> findByCustomer(Long customerId) {
        return ResponseEntity.ok(transferenceService.findByCustomer(customerId));
    }

    @Override
    public ResponseEntity<List<Transference>> findBetweenCustomers(Long firstCustomerId, Long secondCustomerId) {
        return ResponseEntity.ok(transferenceService.findBetweenCustomers(firstCustomerId, secondCustomerId));
    }
}
