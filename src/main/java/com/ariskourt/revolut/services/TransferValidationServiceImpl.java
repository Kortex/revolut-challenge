package com.ariskourt.revolut.services;

import com.ariskourt.revolut.domain.BankAccount;
import com.ariskourt.revolut.exceptions.BankAccountNotFoundException;
import com.ariskourt.revolut.exceptions.InsufficientBalanceException;
import com.ariskourt.revolut.exceptions.SameAccountTransferException;
import com.ariskourt.revolut.exceptions.mappers.ApiExceptionMapper;
import lombok.extern.slf4j.Slf4j;

import javax.enterprise.context.ApplicationScoped;

import java.math.BigDecimal;

@Slf4j
@ApplicationScoped
public class TransferValidationServiceImpl implements TransferValidationService {

    private static final Double INSUFFICIENT_BALANCE = 0.0;

    /***
     * Method that perform all the validation steps for a given a pair of {@link BankAccount} objects,
     * as well as the given transfer amount. Throws various validation related exceptions that are handled by
     * the {@link ApiExceptionMapper} and translated into the
     * appropriate responses.
     *
     * @param from - The account to transfer funds from
     * @param to - The account to transfer funds to
     * @param transferAmount - The amount to transfer between accounts
     */
    @Override
    public void validateTransferDetails(BankAccount from, BankAccount to, BigDecimal transferAmount) {

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

	if (from.getAccountBalance().compareTo(BigDecimal.ZERO) <= 0) {
	    log.error("From account has an insufficient balance. Cannot transfer funds");
	    throw new InsufficientBalanceException("From account has an insufficient balance. Cannot transfer from it");
	}

	if (from.getAccountBalance().compareTo(transferAmount.abs()) < 0) {
	    log.error("From account's current balance is less that the requested transfer amount.");
	    throw new InsufficientBalanceException("From account's balance is lower that the requested transfer amount");
	}

    }

}
