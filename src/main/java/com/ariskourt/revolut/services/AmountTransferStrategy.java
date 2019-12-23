package com.ariskourt.revolut.services;

import com.ariskourt.revolut.domain.BankAccount;

import java.util.function.BiConsumer;

/***
 * Marker interface providing a unified type
 */
public interface AmountTransferStrategy extends BiConsumer<BankAccount, BankAccount> {

}
