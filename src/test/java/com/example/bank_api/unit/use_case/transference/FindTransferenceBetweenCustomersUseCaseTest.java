package com.example.bank_api.unit.use_case.transference;

import com.example.bank_api.config.exception.UnprocessableEntityException;
import com.example.bank_api.domain.model.Transference;
import com.example.bank_api.domain.use_case.transference.FindTransferenceBetweenCustomersUseCase;
import com.example.bank_api.infra.repository.TransferenceRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class FindTransferenceBetweenCustomersUseCaseTest {

    @Mock
    TransferenceRepository transferenceRepository;

    @InjectMocks
    FindTransferenceBetweenCustomersUseCase findTransferenceBetweenCustomersUseCase;

    @Test
    void whenExecuteWithSameCustomers_thenThrow() {
        assertThrows(UnprocessableEntityException.class, () -> findTransferenceBetweenCustomersUseCase.execute(1L, 1L));

        verify(transferenceRepository, times(0)).findByCustomer(any(), any());
    }

    @Test
    void whenExecute_thenReturnTransferenceList() {
        Transference transference = new Transference(1L, 1L, 2L, 100.00, null);
        List<Transference> transferenceList = List.of(transference);
        when(transferenceRepository.findByCustomer(1L, 2L)).thenReturn(transferenceList);

        findTransferenceBetweenCustomersUseCase.execute(1L, 2L);

        verify(transferenceRepository, times(1)).findByCustomer(1L, 2L);
    }
}
