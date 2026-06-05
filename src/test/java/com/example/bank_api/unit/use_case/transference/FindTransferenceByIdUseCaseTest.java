package com.example.bank_api.unit.use_case.transference;

import com.example.bank_api.config.exception.NotFoundException;
import com.example.bank_api.domain.model.Transference;
import com.example.bank_api.domain.use_case.transference.FindTransferenceByIdUseCase;
import com.example.bank_api.infra.repository.TransferenceRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class FindTransferenceByIdUseCaseTest {

    @Mock
    TransferenceRepository transferenceRepository;

    @InjectMocks
    FindTransferenceByIdUseCase findTransferenceByIdUseCase;

    @Test
    void whenExecute_thenReturnTransference() {
        Transference transference = new Transference(1L, 1L, 2L, 100.00, null);
        when(transferenceRepository.findById(1L)).thenReturn(Optional.of(transference));

        findTransferenceByIdUseCase.execute(1L);

        verify(transferenceRepository, times(1)).findById(1L);
    }

    @Test
    void whenExecute_thenThrowNotFoundException() {
        when(transferenceRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> findTransferenceByIdUseCase.execute(1L));

        verify(transferenceRepository, times(1)).findById(1L);
    }
}
