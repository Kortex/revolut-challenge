package com.ariskourt.revolut.services;

import com.ariskourt.revolut.api.resources.AccountTransferRequest;
import com.ariskourt.revolut.domain.BankAccount;
import com.ariskourt.revolut.exceptions.BankAccountNotFoundException;
import com.ariskourt.revolut.exceptions.InsufficientBalanceException;
import com.ariskourt.revolut.exceptions.SameAccountTransferException;
import com.ariskourt.revolut.utils.Pair;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.dbutils.handlers.BeanHandler;

import javax.enterprise.context.ApplicationScoped;

import java.sql.SQLException;

@Slf4j
@ApplicationScoped
@RequiredArgsConstructor
public class AccountValidationService {

    private static final String QUERY_BY_ID = "SELECT id, "
        + "account_holder AS accountHolder, "
        + "account_balance AS accountBalance,"
        + "created_at AS createdAt, "
        + "updated_at AS updatedAt, "
        + "version "
        + "FROM bank_account WHERE id = ?";

    private final QueryRunnerService databaseService;

    public Pair<BankAccount, BankAccount> validateAndGetAccounts(AccountTransferRequest request) throws SQLException {

        var fromAccount = databaseService
            .get()
            .query(QUERY_BY_ID, new BeanHandler<>(BankAccount.class), request.getFromAccount());

        var toAccount = databaseService
            .get()
            .query(QUERY_BY_ID, new BeanHandler<>(BankAccount.class), request.getToAccount());

       if (fromAccount == null && toAccount == null) {
           log.error("No bank accounts with id {} and {} have been found", request.getFromAccount(), request.getToAccount());
           throw new BankAccountNotFoundException("No fromAccount and toAccount found with id {1} and {2} have been found",
               request.getFromAccount(), request.getToAccount());
       }

       if (fromAccount == null) {
           log.error("No fromAccount with id {} has been found", request.getFromAccount());
           throw new BankAccountNotFoundException("No fromAccount with the given id {1} has been found", request.getFromAccount());
       }

       if (toAccount == null) {
           log.error("No toAccount with id {} has been found", request.getToAccount());
           throw new BankAccountNotFoundException("No toAccount with id {1} has been found", request.getToAccount());
       }

        if (fromAccount.equals(toAccount)) {
            log.error("Transferring from and to the same account is not applicable");
            throw new SameAccountTransferException("FromAccount and ToAccount are the same. Cannot perform transfer");
        }

        if (fromAccount.getAccountBalance() <= 0L || (fromAccount.getAccountBalance() < request.getAmount())) {
            log.error("FromAccount has an insufficient balance");
            throw new InsufficientBalanceException("FromAccount with id {1} has an insufficient account balance {2}",
                fromAccount.getId(),
                fromAccount.getAccountBalance());
        }

        return Pair.of(fromAccount, toAccount);
    }

}
