package com.ariskourt.revolut.services;

import com.ariskourt.revolut.domain.BankAccount;

import java.util.List;

public interface AccountQueryService {

    BankAccount getAccountBy(String id);
    List<BankAccount> getAccounts();

}
