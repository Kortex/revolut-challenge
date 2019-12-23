package com.ariskourt.revolut.services;

import com.ariskourt.revolut.domain.BankAccount;

public interface TransferValidationService {

    void validateTransferDetails(BankAccount from, BankAccount to, Long transferAmount);

}
