package com.example.bank_api.service;

import com.example.bank_api.client.ApiClient;
import com.example.bank_api.client.dto.SendNotificationResponse;
import com.example.bank_api.exception.NotFoundException;
import com.example.bank_api.exception.UnprocessableEntityException;
import com.example.bank_api.model.Balance;
import com.example.bank_api.model.Transference;
import com.example.bank_api.repository.BalanceRepository;
import com.example.bank_api.repository.CustomerRepository;
import com.example.bank_api.repository.TransferenceRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TransferenceService {
    TransferenceRepository transferenceRepository;
    BalanceRepository balanceRepository;
    CustomerRepository customerRepository;
    ApiClient apiClient;

    public void transfer(Transference transference){
        if (transference.getPayer().equals(transference.getPayee()))
            throw new UnprocessableEntityException("Payer and payee should be different");

        if (customerRepository.findIsBusinessById(transference.getPayer()))
            throw new UnprocessableEntityException("Payer should not be a business");

        Optional<Balance> payerBalance = balanceRepository.findByCustomerId(transference.getPayer());
        if (payerBalance.isEmpty())
            throw new NotFoundException();

        Optional<Balance> payeeBalance = balanceRepository.findByCustomerId(transference.getPayee());
        if (payeeBalance.isEmpty())
            throw new NotFoundException();

        if (payerBalance.get().getAmount() < transference.getValue()){
            throw new UnprocessableEntityException("Not enough balance");
        }

        payerBalance.get().setAmount(payerBalance.get().getAmount() - transference.getValue());
        payeeBalance.get().setAmount(payeeBalance.get().getAmount() + transference.getValue());

        balanceRepository.save(payerBalance.get());
        balanceRepository.save(payeeBalance.get());

        Transference newTransference = transferenceRepository.save(transference);

        if(!apiClient.authorize().getData().getAuthorization()){
            refund(newTransference.getId());
        }

        sendNotification();
    }

    public void refund(Long transferenceId){
        Transference transference = transferenceRepository.getReferenceById(transferenceId);

        Optional<Balance> payerBalance = balanceRepository.findByCustomerId(transference.getPayer());
        if (payerBalance.isEmpty()) throw new NotFoundException();

        Optional<Balance> payeeBalance = balanceRepository.findByCustomerId(transference.getPayee());
        if (payeeBalance.isEmpty()) throw new NotFoundException();

        if (payeeBalance.get().getAmount() < transference.getValue()){
            throw new UnprocessableEntityException("Not enough balance");
        }

        payerBalance.get().setAmount(payerBalance.get().getAmount() + transference.getValue());
        payeeBalance.get().setAmount(payeeBalance.get().getAmount() - transference.getValue());

        balanceRepository.save(payerBalance.get());
        balanceRepository.save(payeeBalance.get());

        transferenceRepository.delete(transference);
    }

    private void sendNotification(){
        String status;
        do {
            SendNotificationResponse response = apiClient.sendNotification();
            status = response.getStatus();
        } while (status.equals("fail"));
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
