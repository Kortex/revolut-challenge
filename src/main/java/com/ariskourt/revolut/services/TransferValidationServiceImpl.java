package com.ariskourt.revolut.services;

import com.ariskourt.revolut.domain.BankAccount;
import com.ariskourt.revolut.exceptions.BankAccountNotFoundException;
import com.ariskourt.revolut.exceptions.InsufficientBalanceException;
import com.ariskourt.revolut.exceptions.SameAccountTransferException;
import com.ariskourt.revolut.exceptions.mappers.ApiExceptionMapper;
import com.ariskourt.revolut.utils.Pair;
import lombok.extern.slf4j.Slf4j;

import javax.enterprise.context.ApplicationScoped;

@Slf4j
@ApplicationScoped
public class TransferValidationServiceImpl implements TransferValidationService {

    private static final Long INSUFFICIENT_BALANCE = 0L;

    /***
     * Method that perform all the validation steps for a given {@link Pair} of {@link BankAccount} object,
     * as well as the given transfer amount. Throws various validation related exceptions that are handled by
     * the {@link ApiExceptionMapper} and translated into the
     * appropriate responses.
     *
     * @param accountPair - The pair of account object to undergo validation
     * @param transferAmount - The amount of funds requested for transfer
     */
    @Override
    public void validateTransferDetails(Pair<BankAccount, BankAccount> accountPair, Long transferAmount) {

        var from = accountPair.getLeft();
        var to = accountPair.getRight();

	if (from == null && to == null) {
	    log.error("No bank account have been found for the given ids");
	    throw new BankAccountNotFoundException("No from and to account have been found for the given ids");
	}

	if (from == null) {
	    log.error("No from account has been for the given id");
	    throw new BankAccountNotFoundException("No from account found for the given id");
	}

	if (to == null) {
	    log.error("No to account has been found for the given id");
	    throw new BankAccountNotFoundException("No to account has been found the given id");
	}

	if (from.equals(to)) {
	    log.error("From and to account are the same. Cannot transfer between the same account");
	    throw new SameAccountTransferException("From and to account are the same. Cannot transfer between them.");
	}

	if (from.getAccountBalance() <= INSUFFICIENT_BALANCE) {
	    log.error("From account has an insufficient balance. Cannot transfer funds");
	    throw new InsufficientBalanceException("From account has an insufficient balance. Cannot transfer from it");
	}

	if (from.getAccountBalance() < transferAmount) {
	    log.error("From account's current balance is less that the requested transfer amount.");
	    throw new InsufficientBalanceException("From account's balance is lower that the requested transfer amount");
	}

    }

}
