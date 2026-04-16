package com.example.bank_api.domain.service.transference;

import com.example.bank_api.config.exception.NotFoundException;
import com.example.bank_api.config.exception.UnprocessableEntityException;
import com.example.bank_api.domain.model.Balance;
import com.example.bank_api.domain.model.Transference;
import com.example.bank_api.infra.gateway.ApiGateway;
import com.example.bank_api.infra.gateway.dto.SendNotificationResponse;
import com.example.bank_api.infra.repository.BalanceRepository;
import com.example.bank_api.infra.repository.CustomerRepository;
import com.example.bank_api.infra.repository.TransferenceRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@AllArgsConstructor
public class TransferUseCase {

    private final TransferenceRepository transferenceRepository;
    private final BalanceRepository balanceRepository;
    private final CustomerRepository customerRepository;
    private final RefundUseCase refundUseCase;
    private final ApiGateway apiGateway;

    public void execute(Transference transference) {
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

        if (payerBalance.get().getAmount() < transference.getValue()) {
            throw new UnprocessableEntityException("Not enough balance");
        }

        payerBalance.get().setAmount(payerBalance.get().getAmount() - transference.getValue());
        payeeBalance.get().setAmount(payeeBalance.get().getAmount() + transference.getValue());

        balanceRepository.save(payerBalance.get());
        balanceRepository.save(payeeBalance.get());

        Transference newTransference = transferenceRepository.save(transference);

        if (!apiGateway.authorize().getData().getAuthorization()) {
            refundUseCase.execute(newTransference.getId());
        }

        String status;
        do {
            SendNotificationResponse response = apiGateway.sendNotification();
            status = response.getStatus();
        } while (status.equals("fail"));
    }
}
