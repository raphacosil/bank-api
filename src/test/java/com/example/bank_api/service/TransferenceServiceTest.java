package com.example.bank_api.service;

import com.example.bank_api.exception.NotFoundException;
import com.example.bank_api.exception.UnprocessableEntityException;
import com.example.bank_api.gateway.ApiGateway;
import com.example.bank_api.model.Transference;
import com.example.bank_api.repository.BalanceRepository;
import com.example.bank_api.repository.CustomerRepository;
import com.example.bank_api.repository.TransferenceRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TransferenceServiceTest {

    @Mock
    TransferenceRepository transferenceRepository;
    @Mock
    BalanceRepository balanceRepository;
    @Mock
    CustomerRepository customerRepository;
    @Mock
    ApiGateway apiGateway;

    @InjectMocks
    TransferenceService transferenceService;

//    public List<Transference> findAll(){
//        return transferenceRepository.findAll();
//    }
//    public Transference findById(Long transferenceId){
//        Optional<Transference> transference = transferenceRepository.findById(transferenceId);
//
//        if(transference.isEmpty()){
//            throw new NotFoundException();
//        }
//
//        return transference.get();
//    }
//
//    public List<Transference> findByCustomer(Long customerId){
//        return transferenceRepository.findByCustomer(customerId);
//    }
//
//    public List<Transference> findBetweenCustomers(Long firstCustomerId, Long secondCustomerId){
//        return transferenceRepository.findByCustomer(firstCustomerId, secondCustomerId);
//    }


    @Test
    void whenFindAll_thenReturnCustomerList() {
        Transference transference = new Transference(1L, 1L, 2L, 100.00, null);
        List<Transference> transferenceList = List.of(transference);
        when(transferenceRepository.findAll()).thenReturn(transferenceList);

        transferenceService.findAll();

        verify(transferenceRepository, times(1)).findAll();
    }

    @Test
    void whenFindByCustomer_thenReturnCustomerList() {
        Transference transference = new Transference(1L, 1L, 2L, 100.00, null);
        List<Transference> transferenceList = List.of(transference);
        when(transferenceRepository.findByCustomer(1L)).thenReturn(transferenceList);

        transferenceService.findByCustomer(1L);

        verify(transferenceRepository, times(1)).findByCustomer(1L);
    }

    @Test
    void whenFindBetweenCustomers_thenThrow() {
        Transference transference = new Transference(1L, 1L, 2L, 100.00, null);
        List<Transference> transferenceList = List.of(transference);

        assertThrows(UnprocessableEntityException.class, () -> transferenceService.findBetweenCustomers(1L, 1L));

        verify(transferenceRepository, times(0)).findByCustomer(any(), any());
    }

    @Test
    void whenFindBetweenCustomers_thenReturnCustomerList() {
        Transference transference = new Transference(1L, 1L, 2L, 100.00, null);
        List<Transference> transferenceList = List.of(transference);
        when(transferenceRepository.findByCustomer(1L, 2L)).thenReturn(transferenceList);

        transferenceService.findBetweenCustomers(1L, 2L);

        verify(transferenceRepository, times(1)).findByCustomer(1L, 2L);
    }

    @Test
    void whenFindById_thenReturnCustomer() {
        Transference transference = new Transference(1L, 1L, 2L, 100.00, null);
        when(transferenceRepository.findById(1L)).thenReturn(Optional.of(transference));

        transferenceService.findById(1L);

        verify(transferenceRepository, times(1)).findById(1L);
    }

    @Test
    void whenFindById_thenThrowNotFoundException(){
        when(transferenceRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> transferenceService.findById(1L));

        verify(transferenceRepository, times(1)).findById(1L);
    }
}
