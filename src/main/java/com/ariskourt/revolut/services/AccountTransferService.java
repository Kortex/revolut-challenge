package com.ariskourt.revolut.services;

import com.ariskourt.revolut.api.resources.AccountTransferRequest;
import com.ariskourt.revolut.api.resources.AccountTransferResponse;
import com.ariskourt.revolut.exceptions.InsufficientBalanceException;
import com.ariskourt.revolut.exceptions.SameAccountTransferException;
import com.ariskourt.revolut.repositories.BankAccountRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import javax.enterprise.context.ApplicationScoped;
import javax.transaction.Transactional;

import java.util.UUID;

@Slf4j
@ApplicationScoped
@RequiredArgsConstructor
public class AccountTransferService {

    private final BankAccountRepository repository;
    private final AccountPresenceValidationService validationService;

    @Transactional
    public AccountTransferResponse transferAmount(AccountTransferRequest request) {
        log.info("Got incoming request with details {} validating", request);
        validationService.validateRequest(request);

        var fromAccount = repository.findById(request.getFromAccount());
        var toAccount = repository.findById(request.getToAccount());

        if (fromAccount.equals(toAccount)) {
            log.error("Transferring from and to the same account is not applicable");
            throw new SameAccountTransferException("FromAccount and ToAccount are the same. Cannot perform transfer");
        }

        if (fromAccount.getAccountBalance() < 0L || (fromAccount.getAccountBalance() < request.getAmount())) {
            log.error("FromAccount has an insufficient balance");
            throw new InsufficientBalanceException("FromAccount with id {1} has an insufficient account balance {2}",
                fromAccount.getId(),
                fromAccount.getAccountBalance());
        }

        log.info("Transferring {} credits from account {} to account {}", request.getAmount(),
            fromAccount.getId(), toAccount.getId());

        fromAccount.setAccountBalance(fromAccount.getAccountBalance() - request.getAmount());
        toAccount.setAccountBalance(toAccount.getAccountBalance() + request.getAmount());

        fromAccount.persistAndFlush();
        toAccount.persistAndFlush();

        return new AccountTransferResponse(request.getAmount(), "Account funds transfer has been successful");

    }

}
