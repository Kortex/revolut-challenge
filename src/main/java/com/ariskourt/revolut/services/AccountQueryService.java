package com.ariskourt.revolut.services;

import com.ariskourt.revolut.domain.BankAccount;

public interface AccountQueryService {

    BankAccount getBankAccount(String id);

}
