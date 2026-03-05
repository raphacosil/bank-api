package com.example.bank_api.service;

import com.example.bank_api.exception.NotFoundException;
import com.example.bank_api.exception.UnprocessableEntityException;
import com.example.bank_api.model.Balance;
import com.example.bank_api.model.Transference;
import com.example.bank_api.repository.BalanceRepository;
import com.example.bank_api.repository.TransferenceRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class TransferenceService {
    TransferenceRepository transferenceRepository;
    BalanceRepository balanceRepository;

    public void transfer(Long payerId, Long receiverId, Double amount){
        Optional<Balance> payerBalance = balanceRepository.findByCustomerId(payerId);
        if (payerBalance.isEmpty()) throw new NotFoundException();

        Optional<Balance> receiverBalance = balanceRepository.findByCustomerId(receiverId);
        if (receiverBalance.isEmpty()) throw new NotFoundException();

        if (payerBalance.get().getAmount() < amount){
            throw new UnprocessableEntityException("Not enough balance");
        }

        payerBalance.get().setAmount(payerBalance.get().getAmount() - amount);
        receiverBalance.get().setAmount(receiverBalance.get().getAmount() + amount);

        balanceRepository.save(payerBalance.get());
        balanceRepository.save(receiverBalance.get());

        Transference transference = new Transference();

        transference.setPayer_id(payerId);
        transference.setReceiver_id(receiverId);
        transference.setAmount(amount);

        transferenceRepository.save(transference);
    }

    public void refund(Long transferenceId){
        Transference transference = transferenceRepository.getReferenceById(transferenceId);

        Optional<Balance> payerBalance = balanceRepository.findByCustomerId(transference.getPayer_id());
        if (payerBalance.isEmpty()) throw new NotFoundException();

        Optional<Balance> receiverBalance = balanceRepository.findByCustomerId(transference.getReceiver_id());
        if (receiverBalance.isEmpty()) throw new NotFoundException();

        if (receiverBalance.get().getAmount() < transference.getAmount()){
            throw new UnprocessableEntityException("Not enough balance");
        }

        payerBalance.get().setAmount(payerBalance.get().getAmount() + transference.getAmount());
        receiverBalance.get().setAmount(receiverBalance.get().getAmount() - transference.getAmount());

        balanceRepository.save(payerBalance.get());
        balanceRepository.save(receiverBalance.get());

        transferenceRepository.delete(transference);
    }

    public List<Transference> findAll(){
        return transferenceRepository.findAll();
    }
    public Transference findById(Long transferenceId){
        Optional<Transference> transference = transferenceRepository.findById(transferenceId);

        if(transference.isEmpty()){
            throw new NotFoundException();
        }

        return transference.get();
    }

    public List<Transference> findByCustomer(Long customerId){
        return transferenceRepository.findByCustomer(customerId);
    }

    public List<Transference> findBetweenCustomers(Long firstCustomerId, Long secondCustomerId){
        return transferenceRepository.findByCustomer(firstCustomerId, secondCustomerId);
    }

//    public void transfer(Long payerId, Long receiverId, Double amount){
//        Double payerBalance = transferenceRepository.getBalance(payerId);
//        if (payerBalance < amount){
//            throw new UnprocessableEntityException("Not enough balance");
//        }
//
//        Transference transference = new Transference();
//
//        transference.setPayer_id(payerId);
//        transference.setReceiver_id(receiverId);
//        transference.setAmount(amount);
//
//        transferenceRepository.save(transference);
//    }
//
//    public void refund(Long transferenceId){
//        Transference transference = transferenceRepository.getReferenceById(transferenceId);
//
//        Double receiverBalance = transferenceRepository.getBalance(transference.getReceiver_id());
//
//        if (receiverBalance < transference.getAmount()){
//            throw new UnprocessableEntityException("Not enough balance");
//        }
//
//        transferenceRepository.delete(transference);
//    }
}
