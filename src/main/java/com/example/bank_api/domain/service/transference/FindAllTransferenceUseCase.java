package com.example.bank_api.domain.service.transference;

import com.example.bank_api.domain.model.Transference;
import com.example.bank_api.infra.repository.TransferenceRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@AllArgsConstructor
public class FindAllTransferenceUseCase {

    private final TransferenceRepository transferenceRepository;

    public List<Transference> execute(){
        return transferenceRepository.findAll();
    }
}
