package com.ariskourt.revolut.services;

import com.ariskourt.revolut.domain.BankAccount;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;

/***
 * Simple implementation of transfer strategies that transfer funds between two accounts if the amount is positive
 */
@Slf4j
@RequiredArgsConstructor
class PositiveAmountStrategy implements AmountTransferStrategy {

    private final BigDecimal amount;

    /***
     * Accepts two account objects and transfer the amount between them
     * @param from - The account to transfer from
     * @param to - The account to transfer to
     */
    @Override
    public void accept(BankAccount from, BankAccount to) {
        log.info("Transferring {} funds from {} to {}", amount, from.getId(), to.getId());
        from.subFromBalance(amount);
        to.addToBalance(amount);
    }

}
