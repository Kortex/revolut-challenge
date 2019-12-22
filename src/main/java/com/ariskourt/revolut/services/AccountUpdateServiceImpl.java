package com.ariskourt.revolut.services;

import com.ariskourt.revolut.domain.BankAccount;
import com.ariskourt.revolut.exceptions.DataAccessException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import javax.enterprise.context.ApplicationScoped;

import java.sql.SQLException;

@Slf4j
@ApplicationScoped
@RequiredArgsConstructor
public class AccountUpdateServiceImpl implements AccountUpdateService {

    private static final String UPDATE_QUERY = "UPDATE bank_account "
	+ "SET account_balance = ?, "
	+ "updated_at = NOW(), "
	+ "version = ((SELECT version FROM bank_account WHERE id = ?) + 1) "
	+ "WHERE id = ?";

    private final QueryRunnerService queryRunnerService;

    /***
     * Method that accepts a {@link BankAccount} object, creates an UPDATE sql statement using the object's
     * only updatable field (accountBalance) and performs the database update. Returns nothing but can throw
     * a {@link DataAccessException} in case a database related operation fails
     *
     * @param account - The account object to update
     */
    @Override
    public void updateAccount(BankAccount account) {
	log.info("Attempting to update the following account object {}", account);
	try {
	    var affected = queryRunnerService
		.get()
		.update(UPDATE_QUERY,
		    account.getAccountBalance(),
		    account.getId(),
		    account.getId());
	    log.info("Updated {} rows for the following account id {}", affected, account.getId());
	} catch (SQLException e) {
	    log.error("Attempting to update a bank account object {} has failed", account, e);
	    throw new DataAccessException(e);
	}

    }

}
