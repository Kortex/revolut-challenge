package com.ariskourt.revolut.services;

import com.ariskourt.revolut.domain.BankAccount;
import com.ariskourt.revolut.utils.Pair;

public interface TransferValidationService {

    void validateTransferDetails(Pair<BankAccount, BankAccount> accountPair, Long transferAmount);

}
