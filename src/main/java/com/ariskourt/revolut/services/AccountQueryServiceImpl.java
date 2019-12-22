package com.ariskourt.revolut.services;

import com.ariskourt.revolut.domain.BankAccount;
import com.ariskourt.revolut.exceptions.DataAccessException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.dbutils.handlers.BeanHandler;

import javax.enterprise.context.ApplicationScoped;

import java.sql.SQLException;

@Slf4j
@ApplicationScoped
@RequiredArgsConstructor
public class AccountQueryServiceImpl implements AccountQueryService {

    private static final String QUERY = "SELECT id, "
	+ "account_holder AS accountHolder, "
	+ "account_balance AS accountBalance,"
	+ "created_at AS createdAt, "
	+ "updated_at AS updatedAt, "
	+ "version "
	+ "FROM bank_account WHERE id = ?";

    private final QueryRunnerService queryRunnerService;

    /***
     * Method that accepts a string id, constructs the appropriate SELECT sql statement and queries the database
     * for the given id. Returns a concrete {@link BankAccount} if the id is matched or null if not. Can throw a
     * {@link DataAccessException} in case a database related operation fails
     *
     * @param id - The string id used to query the database
     * @return - A the fully constructed account object
     */
    @Override
    public BankAccount getBankAccount(String id) {
	log.info("Querying database for account with the following id {}", id);
	try {
	    return queryRunnerService
		.get()
		.query(QUERY, new BeanHandler<>(BankAccount.class), id);
	} catch (SQLException e) {
	    log.error("Attempting to query for bank account with id {} has failed", id, e);
	    throw new DataAccessException(e);
	}

    }

}
