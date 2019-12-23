package com.ariskourt.revolut.services;

import com.ariskourt.revolut.domain.BankAccount;

import java.util.function.BiConsumer;

/***
 * Marker interface extending BiConsumer providing a unified type between strategies
 */
public interface AmountTransferStrategy extends BiConsumer<BankAccount, BankAccount> {

}
