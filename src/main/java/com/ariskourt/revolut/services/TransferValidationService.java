package com.ariskourt.revolut.services;

import com.ariskourt.revolut.domain.BankAccount;

import java.math.BigDecimal;

public interface TransferValidationService {

    void validateTransferDetails(BankAccount from, BankAccount to, BigDecimal transferAmount);

}
