package com.example.bank_api.domain.service.transference;

import com.example.bank_api.config.exception.UnprocessableEntityException;
import com.example.bank_api.domain.model.Transference;
import com.example.bank_api.infra.repository.TransferenceRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@AllArgsConstructor
public class FindTransferenceBetweenCustomersUseCase {

    private final TransferenceRepository transferenceRepository;

    public List<Transference> execute(Long firstCustomerId, Long secondCustomerId) {
        if (firstCustomerId.equals(secondCustomerId)) {
            throw new UnprocessableEntityException("Customers should be different");
        }
        return transferenceRepository.findByCustomer(firstCustomerId, secondCustomerId);
    }
}
