package com.example.bank_api.boundary.controller;

import com.example.bank_api.boundary.contract.TransferenceContract;
import com.example.bank_api.config.exception.BadRequestException;
import com.example.bank_api.domain.model.Transference;
import com.example.bank_api.domain.service.transference.*;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Objects;

@Tag(name = "Transference", description = "Endpoints for managing transferences")
@RestController
@RequestMapping("/transference")
@AllArgsConstructor
public class TransferenceController implements TransferenceContract {

    private final TransferUseCase transferUseCase;
    private final RefundUseCase refundUseCase;
    private final FindAllTransferenceUseCase findAllTransferenceUseCase;
    private final FindTransferenceByIdUseCase findTransferenceByIdUseCase;
    private final FindTransferenceByCustomerUseCase findTransferenceByCustomerUseCase;
    private final FindTransferenceBetweenCustomersUseCase findTransferenceBetweenCustomersUseCase;

    @Override
    public ResponseEntity<Void> transfer(Transference transference, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new BadRequestException(
                    Objects.requireNonNull(bindingResult.getFieldError()).getDefaultMessage()
            );
        }

        transferUseCase.execute(transference);

        return ResponseEntity.status(200).build();
    }

    @Override
    public ResponseEntity<Void> refund(Long transferenceId) {
        refundUseCase.execute(transferenceId);

        return ResponseEntity.status(200).build();
    }

    @Override
    public ResponseEntity<List<Transference>> findAll() {
        return ResponseEntity.ok(findAllTransferenceUseCase.execute());
    }

    @Override
    public ResponseEntity<Transference> findById(Long id) {
        return ResponseEntity.ok(findTransferenceByIdUseCase.execute(id));
    }

    @Override
    public ResponseEntity<List<Transference>> findByCustomer(Long customerId) {
        return ResponseEntity.ok(findTransferenceByCustomerUseCase.execute(customerId));
    }

    @Override
    public ResponseEntity<List<Transference>> findBetweenCustomers(Long firstCustomerId, Long secondCustomerId) {
        return ResponseEntity.ok(findTransferenceBetweenCustomersUseCase.execute(firstCustomerId, secondCustomerId));
    }
}
