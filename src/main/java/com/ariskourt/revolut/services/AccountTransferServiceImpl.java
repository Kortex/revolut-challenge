package com.ariskourt.revolut.services;

import com.ariskourt.revolut.api.AccountTransferRequest;
import com.ariskourt.revolut.api.AccountTransferResponse;
import com.ariskourt.revolut.database.AccountQueryService;
import com.ariskourt.revolut.database.AccountUpdateService;
import com.ariskourt.revolut.utils.OperationTimer;
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

            var amount = request.getAmount();
            var fromAccount = queryService.getAccountBy(request.getFromAccount());
            var toAccount = queryService.getAccountBy(request.getToAccount());

            log.info("Validating fetched accounts and transfer details...");
            validationService.validateTransferDetails(fromAccount, toAccount, amount);

            TransferStrategyFactory
                .getStrategy(amount)
                .accept(fromAccount, toAccount);

            updateService.updateAccount(fromAccount);
            updateService.updateAccount(toAccount);

            return AccountTransferResponse
                .builder()
                .fromAccount(fromAccount.getId())
                .fromBalance(fromAccount.getAccountBalance())
                .toAccount(toAccount.getId())
                .toBalance(toAccount.getAccountBalance())
                .amount(amount)
                .message("Transfer of " + amount + " from " + fromAccount.getId() + " to " + toAccount.getId() + " successful")
                .build();
        } finally {
            LOCK.unlock();
            timer.stop();
            log.info(timer.toString());
        }

    }

}
