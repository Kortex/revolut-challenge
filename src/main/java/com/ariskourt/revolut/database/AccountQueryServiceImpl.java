package com.ariskourt.revolut.database;

import com.ariskourt.revolut.domain.BankAccount;
import com.ariskourt.revolut.exceptions.DataAccessException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;

import javax.enterprise.context.ApplicationScoped;

import java.sql.SQLException;
import java.util.List;

@Slf4j
@ApplicationScoped
@RequiredArgsConstructor
public class AccountQueryServiceImpl implements AccountQueryService {

    private static final String QUERY_SINGLE = "SELECT id, "
	+ "account_holder AS accountHolder, "
	+ "account_balance AS accountBalance,"
	+ "created_at AS createdAt, "
	+ "updated_at AS updatedAt, "
	+ "version "
	+ "FROM bank_account WHERE id = ?";

    private static final String QUERY_MULTI = "SELECT id, "
	+ "account_holder AS accountHolder, "
	+ "account_balance AS accountBalance,"
	+ "created_at AS createdAt, "
	+ "updated_at AS updatedAt, "
	+ "version "
	+ "FROM bank_account";

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
    public BankAccount getAccountBy(String id) {
	log.info("Querying database for account with the following id {}", id);
	try {
	    return queryRunnerService
		.getRunner()
		.query(QUERY_SINGLE, new BeanHandler<>(BankAccount.class), id);
	} catch (SQLException e) {
	    log.error("Attempting to query for bank account with id {} has failed", id, e);
	    throw new DataAccessException(e);
	}

    }

    /***
     * Method that constructs the appropriate SELECT sql statement and queries the database retrieving all records.
     * Returns a list of concrete {@link BankAccount} objects or an empty list if none are found.
     * Can throw a {@link DataAccessException} in case a database related operation fails
     *
     * @return - A the fully constructed account object
     */
    @Override
    public List<BankAccount> getAccounts() {
	log.info("Querying database for accounts to list all accounts");
	try {
	    return queryRunnerService
		.getRunner()
		.query(QUERY_MULTI, new BeanListHandler<>(BankAccount.class));
	} catch (SQLException e) {
	    log.error("Listing bank accounts has failed", e);
	    throw new DataAccessException(e);
	}
    }

}
