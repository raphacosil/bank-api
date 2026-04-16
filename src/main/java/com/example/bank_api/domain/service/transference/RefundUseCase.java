package com.example.bank_api.domain.service.transference;

import com.example.bank_api.config.exception.NotFoundException;
import com.example.bank_api.config.exception.UnprocessableEntityException;
import com.example.bank_api.domain.model.Balance;
import com.example.bank_api.domain.model.Transference;
import com.example.bank_api.infra.repository.BalanceRepository;
import com.example.bank_api.infra.repository.TransferenceRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@AllArgsConstructor
public class RefundUseCase {

    private final TransferenceRepository transferenceRepository;
    private final BalanceRepository balanceRepository;

    public void execute(Long transferenceId) {
        Optional<Transference> transference = transferenceRepository.findById(transferenceId);
        if (transference.isEmpty()) throw new NotFoundException();

        Optional<Balance> payerBalance = balanceRepository.findByCustomerId(transference.get().getPayer());
        if (payerBalance.isEmpty()) throw new NotFoundException();

        Optional<Balance> payeeBalance = balanceRepository.findByCustomerId(transference.get().getPayee());
        if (payeeBalance.isEmpty()) throw new NotFoundException();

        if (payeeBalance.get().getAmount() < transference.get().getValue()) {
            throw new UnprocessableEntityException("Not enough balance");
        }

        payerBalance.get().setAmount(payerBalance.get().getAmount() + transference.get().getValue());
        payeeBalance.get().setAmount(payeeBalance.get().getAmount() - transference.get().getValue());

        balanceRepository.save(payerBalance.get());
        balanceRepository.save(payeeBalance.get());

        transferenceRepository.delete(transference.get());
    }
}
