package com.example.bank_api.unit.use_case.transference;

import com.example.bank_api.domain.model.Transference;
import com.example.bank_api.domain.use_case.transference.FindAllTransferenceUseCase;
import com.example.bank_api.infra.repository.TransferenceRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class FindAllTransferenceUseCaseTest {

    @Mock
    TransferenceRepository transferenceRepository;

    @InjectMocks
    FindAllTransferenceUseCase findAllTransferenceUseCase;

    @Test
    void whenExecute_thenReturnTransferenceList() {
        Transference transference = new Transference(1L, 1L, 2L, 100.00, null);
        List<Transference> transferenceList = List.of(transference);
        when(transferenceRepository.findAll()).thenReturn(transferenceList);

        findAllTransferenceUseCase.execute();

        verify(transferenceRepository, times(1)).findAll();
    }
}
