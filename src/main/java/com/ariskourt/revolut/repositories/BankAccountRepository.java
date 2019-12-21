package com.ariskourt.revolut.repositories;

import com.ariskourt.revolut.domain.BankAccount;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class BankAccountRepository implements PanacheRepositoryBase<BankAccount, String> {
}
