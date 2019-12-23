package com.ariskourt.revolut.services;

import com.ariskourt.revolut.domain.BankAccount;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/***
 * Simple strategy implementation for transferring funds if the amount transferred is negative
 */
@Slf4j
@RequiredArgsConstructor
class NegativeAmountStrategy implements AmountTransferStrategy {

    private final Long amount;

    @Override
    public void accept(BankAccount from, BankAccount to) {
	log.info("Transferring {} funds from {} to {}", amount, from.getId(), to.getId());
	from.addToBalance(amount);
	to.subFromBalance(amount);
    }

}
