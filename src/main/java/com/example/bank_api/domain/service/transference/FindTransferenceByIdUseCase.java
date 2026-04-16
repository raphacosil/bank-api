package com.example.bank_api.domain.service.transference;

import com.example.bank_api.config.exception.NotFoundException;
import com.example.bank_api.domain.model.Transference;
import com.example.bank_api.infra.repository.TransferenceRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;
@Component
@AllArgsConstructor
public class FindTransferenceByIdUseCase {

    private final TransferenceRepository transferenceRepository;

    public Transference execute(Long transferenceId){
        Optional<Transference> transference = transferenceRepository.findById(transferenceId);

        if(transference.isEmpty()){
            throw new NotFoundException();
        }

        return transference.get();
    }
}
