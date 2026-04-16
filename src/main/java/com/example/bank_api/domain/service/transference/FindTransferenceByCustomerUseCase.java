package com.example.bank_api.domain.service.transference;

import com.example.bank_api.domain.model.Transference;
import com.example.bank_api.infra.repository.TransferenceRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@AllArgsConstructor
public class FindTransferenceByCustomerUseCase {

    private final TransferenceRepository transferenceRepository;

    public List<Transference> execute(Long customerId){
        return transferenceRepository.findByCustomer(customerId);
    }
}
