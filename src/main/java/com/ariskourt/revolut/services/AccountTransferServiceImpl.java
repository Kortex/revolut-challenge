package com.ariskourt.revolut.services;

import com.ariskourt.revolut.api.AccountTransferRequest;
import com.ariskourt.revolut.api.AccountTransferResponse;
import com.ariskourt.revolut.utils.OperationTimer;
import com.ariskourt.revolut.utils.Pair;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import javax.enterprise.context.ApplicationScoped;
import javax.transaction.Transactional;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

@Slf4j
@ApplicationScoped
@RequiredArgsConstructor
public class AccountTransferServiceImpl implements AccountTransferService {

    private static final Lock LOCK = new ReentrantLock();

    private final AccountQueryService queryService;
    private final TransferValidationService validationService;
    private final AccountUpdateService updateService;

    @Override
    @Transactional
    public AccountTransferResponse transferAmount(AccountTransferRequest request) {
        log.info("Got incoming request with details {} handling request", request);

        OperationTimer timer = new OperationTimer();
        timer.start();
        LOCK.lock();
        try {
            log.info("Got incoming transfer request with the following details {}", request);

            var fromAccount = queryService.getAccountBy(request.getFromAccount());
            var toAccount = queryService.getAccountBy(request.getToAccount());
            var transferAmount = request.getAmount();

            log.info("Validating fetched accounts and transfer details...");
            validationService.validateTransferDetails(Pair.of(fromAccount, toAccount), transferAmount);

            fromAccount.subFromBalance(transferAmount);
            toAccount.addToBalance(transferAmount);

            updateService.updateAccount(fromAccount);
            updateService.updateAccount(toAccount);

            return AccountTransferResponse
                .builder()
                .fromAccount(fromAccount.getId())
                .fromBalance(fromAccount.getAccountBalance())
                .toAccount(toAccount.getId())
                .toBalance(toAccount.getAccountBalance())
                .amount(transferAmount)
                .message("Transfer of " + transferAmount + " from " + fromAccount.getId() + " to " + toAccount.getId() + " successful")
                .build();
        } finally {
            LOCK.unlock();
            timer.stop();
            log.info(timer.toString());
        }

    }

}
